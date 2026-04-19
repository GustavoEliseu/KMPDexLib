package com.gustavo.kmpdexlib.concept.detail.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gustavo.kmpdexlib.concept.detail.domain.PokemonDetail
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewModel.onEvent(PokemonDetailEvent.LoadPokemon(pokemonId))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                PokemonDetailEffect.NavigateBack -> onBack()
            }
        }
    }

    PokemonDetailContent(
        state = state,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun PokemonDetailContent(
    state: PokemonDetailState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading -> LoadingContent(modifier)
        state.error != null -> ErrorContent(state.error, onBack, modifier)
        state.pokemon != null -> PokemonDetailBody(state.pokemon, onBack, modifier)
    }
}

@Composable
private fun PokemonDetailBody(
    pokemon: PokemonDetail,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val heroColor = pokemonColorNameToColor(pokemon.colorName)
    val heroContentColor = heroColor.contentColorFor()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Hero section ──────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(heroColor)
                .statusBarsPadding()
                .height(240.dp),
        ) {
            // Back button — top start
            TextButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
            ) {
                Text(
                    text = "← Back",
                    color = heroContentColor,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            // Pokédex number — top end, faded
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = heroContentColor.copy(alpha = 0.4f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
            )

            // Sprite — centered
            AsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center),
            )

            // Name — bottom start
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = heroContentColor,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 16.dp),
            )
        }

        // ── Content section ───────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            // Type chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pokemon.types.forEach { typeName ->
                    TypeChip(typeName)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flavor text
            Text(
                text = pokemon.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun TypeChip(
    typeName: String,
    modifier: Modifier = Modifier,
) {
    val background = pokemonTypeToColor(typeName)
    val content = background.contentColorFor()

    Surface(
        color = background,
        shape = RoundedCornerShape(50),
        modifier = modifier,
    ) {
        Text(
            text = typeName.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = content,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("← Back") }
    }
}

private fun pokemonColorNameToColor(colorName: String): Color = when (colorName.lowercase()) {
    "black"  -> Color(90,  90,  88)
    "blue"   -> Color(53,  91,  245)
    "brown"  -> Color(135, 99,  58)
    "gray"   -> Color(160, 162, 165)
    "green"  -> Color(142, 211, 153)
    "pink"   -> Color(251, 202, 224)
    "purple" -> Color(90,  36,  123)
    "red"    -> Color(189, 39,  52)
    "white"  -> Color(220, 220, 220)
    "yellow" -> Color(229, 183, 35)
    else     -> Color(160, 162, 165)
}
