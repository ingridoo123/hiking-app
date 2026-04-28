package com.example.aplikacje_mobline.presentation.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplikacje_mobline.navigation.DrawerHeader

@Composable
fun AppInfoScreen(
    onOpenDrawer: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DrawerHeader(
            title = "Informacje",
            onOpenDrawer = onOpenDrawer,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "Informacje o aplikacji",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Autor: Aleksander Staszewski",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Wersja: 1.0.3",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Aplikacja pomaga przegladac trasy piesze i rowerowe oraz zapisywac ulubione szlaki.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
