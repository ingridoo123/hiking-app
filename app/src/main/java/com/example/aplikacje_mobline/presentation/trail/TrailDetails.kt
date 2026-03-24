package com.example.aplikacje_mobline.presentation.trail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun TrailDetailsScreen(
    trailId: Int,
    viewModel: TrailDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(trailId) {
        viewModel.loadTrail(trailId)
    }

    val trail by viewModel.trail.collectAsStateWithLifecycle()
    val safeTrail = trail ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nie znaleziono szlaku.")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        AsyncImage(
            model = safeTrail.imagePath,
            contentDescription = safeTrail.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = safeTrail.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Trudność: ${safeTrail.difficulty}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Dystans: ${safeTrail.distance}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = safeTrail
                .description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}