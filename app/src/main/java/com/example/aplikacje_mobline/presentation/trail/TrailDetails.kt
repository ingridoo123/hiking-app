package com.example.aplikacje_mobline.presentation.trail

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aplikacje_mobline.data.TrailType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.aplikacje_mobline.R
import com.example.aplikacje_mobline.stopwatch.StopwatchViewModel
import com.example.aplikacje_mobline.stopwatch.StopwatchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailsScreen(
    trailId: Int,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: TrailDetailsViewModel = hiltViewModel(),
    stopwatchViewModel: StopwatchViewModel = hiltViewModel()
) {
    LaunchedEffect(trailId) {
        viewModel.loadTrail(trailId)
    }

    val context = LocalContext.current
    var horizontalDragOffset by remember { mutableFloatStateOf(0f) }
    val trail by viewModel.trail.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val stopwatchUiState by stopwatchViewModel.uiState.collectAsStateWithLifecycle()
    val isStopwatchOwnedByCurrentTrail = stopwatchUiState.activeTrailId == null || stopwatchUiState.activeTrailId == trailId

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Powrot",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                title = { Text("Powrot") },
                actions = {
                    IconButton(onClick = {
                        viewModel.toggleFavorite()
                        onFavoriteClick()
                    }) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            },
                            contentDescription = "Zapisz do ulubionych",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            val safeTrail = trail
            FloatingActionButton(
                onClick = {
                    if (safeTrail != null) {
                        if (stopwatchUiState.activeTrailId != safeTrail.id) {
                            val activeName = stopwatchUiState.activeTrailName ?: "inna trasa"
                            Toast.makeText(
                                context,
                                "Stoper dziala na trasie: $activeName",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@FloatingActionButton
                        }
                        if (stopwatchUiState.isRunning) {
                            Toast.makeText(context, "najpierw zatrzymaj stoper", Toast.LENGTH_SHORT).show()
                            return@FloatingActionButton
                        }
                        if (stopwatchUiState.elapsedMs <= 0L) {
                            Toast.makeText(context, "Brak czasu do udostepnienia", Toast.LENGTH_SHORT).show()
                            return@FloatingActionButton
                        }

                        val shareBody = "Moj czas na trasie ${safeTrail.name}: ${stopwatchUiState.formattedElapsed}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareBody)
                        }
                        context.startActivity(Intent.createChooser(intent, "Udostepnij"))
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Udostepnij czas"
                )
            }
        }
    ) { innerPadding ->
        val swipeToBackModifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    if (dragAmount > 0f) {
                        horizontalDragOffset = (horizontalDragOffset + dragAmount).coerceAtLeast(0f)
                    }
                    change.consume()
                },
                onDragEnd = {
                    if (horizontalDragOffset > 120f) {
                        onBackClick()
                    }
                    horizontalDragOffset = 0f
                },
                onDragCancel = {
                    horizontalDragOffset = 0f
                }
            )
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .then(swipeToBackModifier),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .then(swipeToBackModifier)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = errorMessage.orEmpty(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(onClick = { viewModel.retry(trailId) }) {
                            Text("Sprobuj ponownie")
                        }
                    }
                }
            }
            trail == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .then(swipeToBackModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nie znaleziono szlaku.")
                }
            }
            else -> {
                trail?.let { safeTrail ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .then(swipeToBackModifier)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        AsyncImage(
                            model = safeTrail.imagePath,
                            contentDescription = safeTrail.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        TrailDetailsContentCard(
                            name = safeTrail.name,
                            difficulty = safeTrail.difficulty,
                            distance = safeTrail.distance,
                            type = safeTrail.type,
                            country = safeTrail.country?.takeIf { it.isNotBlank() } ?: "Polska",
                            description = safeTrail.description,
                            stopwatchUiState = stopwatchUiState,
                            isStopwatchOwnedByCurrentTrail = isStopwatchOwnedByCurrentTrail,
                            onStartStopwatch = { stopwatchViewModel.startForTrail(safeTrail.id, safeTrail.name) },
                            onStopStopwatch = { stopwatchViewModel.stopForTrail(safeTrail.id) },
                            onResetStopwatch = { stopwatchViewModel.resetForTrail(safeTrail.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-30).dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrailDetailsContentCard(
    name: String,
    difficulty: String,
    distance: String,
    type: TrailType,
    country: String,
    description: String,
    stopwatchUiState: StopwatchUiState,
    isStopwatchOwnedByCurrentTrail: Boolean,
    onStartStopwatch: () -> Unit,
    onStopStopwatch: () -> Unit,
    onResetStopwatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infoSections = listOf(
        TrailInfoSectionData(label = "Długość:", value = distance),
        TrailInfoSectionData(label = "Typ:", value = type.name.lowercase().replaceFirstChar { it.uppercase() }),
        TrailInfoSectionData(label = "Kraj:", value = country)
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            TrailDifficultyBadges(text = difficulty)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Opis szlaku",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TrailInfoSectionsGrid(sections = infoSections)
            Spacer(modifier = Modifier.height(20.dp))

            StopwatchPanel(
                uiState = stopwatchUiState,
                isStopwatchOwnedByCurrentTrail = isStopwatchOwnedByCurrentTrail,
                onStart = onStartStopwatch,
                onStop = onStopStopwatch,
                onReset = onResetStopwatch
            )
        }
    }
}

@Composable
private fun StopwatchPanel(
    uiState: StopwatchUiState,
    isStopwatchOwnedByCurrentTrail: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Stoper",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (!isStopwatchOwnedByCurrentTrail) {
            Text(
                text = "Stoper dziala na trasie: ${uiState.activeTrailName.orEmpty()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return@Column
        }
        Text(
            text = uiState.formattedElapsed,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onStart,
                enabled = !uiState.isRunning,
                modifier = Modifier.weight(1.6f),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0Xffa11f4e),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Włącz stoper",
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Button(
                onClick = onStop,
                enabled = uiState.isRunning,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("Stop")
            }
            Button(
                onClick = onReset,
                enabled = uiState.elapsedMs > 0L,
                modifier = Modifier.weight(1.5f),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text(
                    text = "Przerwij",
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun TrailInfoSectionsGrid(
    sections: List<TrailInfoSectionData>,
    modifier: Modifier = Modifier
) {
    val fixedSections = listOf(
        sections.getOrNull(0) ?: TrailInfoSectionData("Długość:", "-"),
        sections.getOrNull(1) ?: TrailInfoSectionData("Typ:", "-"),
        sections.getOrNull(2) ?: TrailInfoSectionData("Kraj:", "-")
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        fixedSections.forEach { section ->
            TrailInfoSectionCard(
                section = section,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TrailInfoSectionCard(
    section: TrailInfoSectionData,
    modifier: Modifier = Modifier
) {
    val iconPath = when(section.label) {
        "Długość:" -> R.drawable.material_icons_hiking
        "Typ:" -> R.drawable.material_icons_travel_explore
        "Kraj:" -> R.drawable.font_awesome_location_arrow
        else -> R.drawable.material_icons_hiking
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconPath),
                    contentDescription = section.label,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = section.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = section.value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TrailDifficultyBadges(
    text: String,
    modifier: Modifier = Modifier
) {
    val normalized = text.lowercase()
    val badgesCount = when {
        normalized.contains("trud") -> 3
        normalized.contains("sred") || normalized.contains("śred") -> 2
        else -> 1
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DifficultyBadgeBox(isActive = true)
        Spacer(Modifier.size(4.dp))
        DifficultyBadgeBox(isActive = badgesCount >= 2)
        Spacer(Modifier.size(4.dp))
        DifficultyBadgeBox(isActive = badgesCount >= 3)
    }
}

@Composable
private fun DifficultyBadgeBox(isActive: Boolean) {
    val activeColor = androidx.compose.ui.graphics.Color(0Xffa11f4e)
    val inactiveColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)

    Box(
        modifier = Modifier
            .size(22.dp)
            .background(
                color = if (isActive) activeColor else inactiveColor,
                shape = RoundedCornerShape(8.dp)
            )
    )
}

private data class TrailInfoSectionData(
    val label: String,
    val value: String
)