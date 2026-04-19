package com.gustavo.kmpdexlib.concept.list.features.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pokédex") }) },
        modifier = modifier,
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            items(placeholderPokemons) { (id, name) ->
                PokemonListItem(
                    id = id,
                    name = name,
                    onClick = { onPokemonClick(id) },
                )
            }
        }
    }
}

@Composable
private fun PokemonListItem(
    id: Int,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "#${id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.width(52.dp),
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

private val placeholderPokemons = listOf(
    1 to "Bulbasaur",
    4 to "Charmander",
    7 to "Squirtle",
    25 to "Pikachu",
    133 to "Eevee",
)
