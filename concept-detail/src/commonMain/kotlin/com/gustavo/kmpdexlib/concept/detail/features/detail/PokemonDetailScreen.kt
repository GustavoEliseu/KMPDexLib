package com.gustavo.kmpdexlib.concept.detail.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("#${pokemonId.toString().padStart(3, '0')}") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← Back")
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Text(
                text = "Name: —",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Description: —",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
