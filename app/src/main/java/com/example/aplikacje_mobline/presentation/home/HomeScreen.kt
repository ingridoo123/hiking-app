package com.example.aplikacje_mobline.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aplikacje_mobline.R
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailType
import com.example.aplikacje_mobline.navigation.Screen


@Composable
fun HomeScreen(
    navController: NavController,
    onOpenDrawer: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val selectedType by viewModel.selectedType.collectAsState()
    val filteredTrails by viewModel.filteredTrails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        bottomBar = { }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            com.example.aplikacje_mobline.navigation.DrawerHeader(
                title = "Home",
                onOpenDrawer = onOpenDrawer
            )

            Text(
                text = stringResource(R.string.home_one),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
            )

            TrailTypeSwitcher(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                selectedType = selectedType,
                hikingLabel = stringResource(R.string.home_button_one),
                bikingLabel = stringResource(R.string.home_button_two),
                onSelect = viewModel::selectType
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    errorMessage != null -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = errorMessage.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            OutlinedButton(onClick = viewModel::retry) {
                                Text(stringResource(R.string.home_button_error))
                            }
                        }
                    }
                    filteredTrails.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.no_trails_for_type),
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredTrails) {
                                TrailCard(trail = it, navController = navController)
                            }
                        }
                    }
                }
            }
        }


    }


}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun TrailTypeSwitcher(
    selectedType: TrailType,
    hikingLabel: String,
    bikingLabel: String,
    onSelect: (TrailType) -> Unit,
    modifier: Modifier = Modifier
) {
    val switcherShape = RoundedCornerShape(18.dp)
    BoxWithConstraints(
        modifier = modifier
            .clip(switcherShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                shape = switcherShape
            )
            .padding(5.dp)
    ) {
        val segmentWidth = maxWidth / 2
        val transition = updateTransition(targetState = selectedType, label = "type_transition")
        val indicatorOffset by transition.animateDp(
            transitionSpec = { tween(durationMillis = 450) },
            label = "indicator_offset"
        ) { type ->
            if (type == TrailType.HIKING) 0.dp else segmentWidth
        }

        val flow = rememberInfiniteTransition(label = "flow")
        val flowShift by flow.animateFloat(
            initialValue = -200f,
            targetValue = 600f,
            animationSpec = infiniteRepeatable(animation = tween(durationMillis = 2600)),
            label = "flow_shift"
        )

        Box(
            modifier = Modifier
                .padding(end = 5.dp)
                .offset(x = indicatorOffset)
                .width(segmentWidth - 5.dp)
                .height(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(flowShift - 220f, 0f),
                        end = androidx.compose.ui.geometry.Offset(flowShift, 180f)
                    )
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            TrailTypeSegment(
                label = hikingLabel,
                isSelected = selectedType == TrailType.HIKING,
                onClick = { onSelect(TrailType.HIKING) },
                modifier = Modifier.weight(1f)
            )
            TrailTypeSegment(
                label = bikingLabel,
                isSelected = selectedType == TrailType.BIKING,
                onClick = { onSelect(TrailType.BIKING) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TrailTypeSegment(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(durationMillis = 320)
    )

    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TrailCard(
    trail: Trail,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clickable {
                navController.navigate(Screen.TrailDetails.createRoute(trail.id))
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = trail.imagePath,
                contentDescription = trail.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.general_img_landscape)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = trail.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                DifficultyBadges(trail.difficulty)

                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TrailMetaBadge(text = trail.distance)
                    TrailMetaBadge(text = trail.country?.takeIf { it.isNotBlank() } ?: "Polska")
                }

            }
        }
    }
}

@Composable
private fun TrailMetaBadge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun DifficultyBadges(
    text: String,
    modifier: Modifier = Modifier
) {
    var badgesCount by remember { mutableIntStateOf(1) }
    val badgeColor = Color(0Xffa11f4e)

    if(text == "Łatwa") {
        badgesCount = 1
    } else if(text == "Średnia") {
        badgesCount = 2
    } else if (text == "Trudna") {
        badgesCount = 3
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(badgeColor, shape = RoundedCornerShape(8.dp))
            )
        Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(if (badgesCount >= 2) {
                        badgeColor
                    } else MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(8.dp))

            )
        Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(if (badgesCount == 3) {
                        badgeColor
                    } else MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(5.dp))
            )

    }
}

