package com.example.aplikacje_mobline.presentation.trail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplikacje_mobline.data.TrailType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.aplikacje_mobline.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailsScreen(
    trailId: Int,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    viewModel: TrailDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(trailId) {
        viewModel.loadTrail(trailId)
    }

    val trail by viewModel.trail.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
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
                    IconButton(onClick = onFavoriteClick) {
                        // TODO: Replace with your custom icon path/painterResource if needed.
                        Icon(
                            painter = painterResource(R.drawable.bootstrap_bookmark),
                            contentDescription = "Zapisz do ulubionych",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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
                        .padding(innerPadding),
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
                            description = safeTrail.description,
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
    description: String,
    modifier: Modifier = Modifier
) {
    val infoSections = listOf(
        TrailInfoSectionData(label = "Dlugosc trasy:", value = distance),
        TrailInfoSectionData(label = "Typ:", value = type.name.lowercase().replaceFirstChar { it.uppercase() })
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
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun TrailInfoSectionsGrid(
    sections: List<TrailInfoSectionData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sections.chunked(2).forEach { rowSections ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSections.forEach { section ->
                    TrailInfoSectionCard(
                        section = section,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowSections.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TrailInfoSectionCard(
    section: TrailInfoSectionData,
    modifier: Modifier = Modifier
) {
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
                    imageVector = Icons.Filled.Info,
                    contentDescription = section.label,
                    modifier = Modifier.size(16.dp),
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