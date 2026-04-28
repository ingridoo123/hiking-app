package com.example.aplikacje_mobline.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.aplikacje_mobline.R
import com.example.aplikacje_mobline.presentation.info.AppInfoScreen
import com.example.aplikacje_mobline.presentation.favorite.FavoritesScreen
import com.example.aplikacje_mobline.presentation.home.HomeScreen
import com.example.aplikacje_mobline.presentation.trail.TrailDetailsScreen
import com.example.aplikacje_mobline.stopwatch.StopwatchViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val currentTrailId = navBackStackEntry?.arguments?.getString("trailId")?.toIntOrNull()
    val stopwatchViewModel: StopwatchViewModel = hiltViewModel()
    val stopwatchUiState by stopwatchViewModel.uiState.collectAsStateWithLifecycle()
    var stopwatchMenuExpanded by remember { mutableStateOf(false) }
    var stopwatchYOffsetPx by remember { mutableFloatStateOf(0f) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val draggableLimitPx = with(density) {
        (configuration.screenHeightDp.dp.toPx() / 2f) - 90.dp.toPx()
    }
    val drawerRoutes = setOf(Screen.Home.route, Screen.Favorites.route, Screen.AppInfo.route)
    val shouldEnableDrawerGestures = currentRoute in drawerRoutes
    val isOnActiveTrailDetails = currentRoute == Screen.TrailDetails.route &&
        currentTrailId != null &&
        currentTrailId == stopwatchUiState.activeTrailId
    val shouldShowStopwatchBubble = stopwatchUiState.activeTrailId != null &&
        stopwatchUiState.elapsedMs > 0L &&
        !isOnActiveTrailDetails
    val drawerItems = listOf(
        DrawerDestination(Screen.Home.route, "Home", Icons.Filled.Home),
        DrawerDestination(Screen.Favorites.route, "Ulubione", Icons.Filled.Favorite),
        DrawerDestination(Screen.AppInfo.route, "Informacje o aplikacji", Icons.Filled.Info)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = shouldEnableDrawerGestures,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(route = Screen.Home.route) {
                    HomeScreen(
                        navController = navController,
                        onOpenDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
                composable(route = Screen.Favorites.route) {
                    FavoritesScreen(
                        navController = navController,
                        onOpenDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
                composable(route = Screen.AppInfo.route) {
                    AppInfoScreen(
                        onOpenDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
                composable(
                    route = Screen.TrailDetails.route,
                    arguments = listOf(navArgument("trailId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val trailId = backStackEntry.arguments?.getString("trailId")
                    TrailDetailsScreen(
                        trailId = trailId?.toInt() ?: 0,
                        onBackClick = { navController.navigateUp() },
                        onFavoriteClick = { /* TODO: Add save-to-favorites action. */ }
                    )
                }
            }

            if (shouldShowStopwatchBubble) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset { IntOffset(x = 0, y = stopwatchYOffsetPx.roundToInt()) }
                        .padding(end = 12.dp)
                        .pointerInput(draggableLimitPx) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                stopwatchYOffsetPx =
                                    (stopwatchYOffsetPx + dragAmount.y).coerceIn(-draggableLimitPx, draggableLimitPx)
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .widthIn(max = 190.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
                                        androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
                                        androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                                    )
                                )
                            )
                            .clickable { stopwatchMenuExpanded = true }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                            androidx.compose.foundation.layout.Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.lucide_timer),
                                    contentDescription = "Stoper"
                                )
                                Text(
                                    text = stopwatchUiState.formattedElapsed,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Text(
                                text = stopwatchUiState.activeTrailName.orEmpty(),
                                modifier = Modifier.padding(top = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = stopwatchMenuExpanded,
                        onDismissRequest = { stopwatchMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Przerwij") },
                            onClick = {
                                stopwatchViewModel.forceReset()
                                stopwatchMenuExpanded = false
                            },
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerMenuButton(
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onOpenDrawer,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Otworz menu"
        )
    }
}

@Composable
fun DrawerHeader(
    title: String,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        DrawerMenuButton(onOpenDrawer = onOpenDrawer)
        Text(text = title)
    }
}

private data class DrawerDestination(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)