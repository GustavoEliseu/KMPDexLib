package com.gustavo.kmpdexlib.concept.list.features.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.gustavo.kmpdexlib.concept.list.domain.PokemonSummary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PokemonListScreen(
    onPokemonClick: (Int) -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PokemonListEffect.NavigateToDetail -> onPokemonClick(effect.id)
            }
        }
    }

    PokemonListContent(
        state = state,
        onEvent = viewModel::onEvent,
        onClose = onClose,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListContent(
    state: PokemonListState,
    onEvent: (PokemonListEvent) -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex") },
                navigationIcon = {
                    if (onClose != null) {
                        IconButton(onClick = onClose) {
                            Text("✕", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingContent(Modifier.padding(paddingValues))
            state.error != null -> ErrorContent(
                message = state.error,
                onRetry = { onEvent(PokemonListEvent.LoadPokemons) },
                modifier = Modifier.padding(paddingValues),
            )
            else -> PokemonGrid(
                pokemons = state.pokemons,
                isLoadingMore = state.isLoadingMore,
                onPokemonClick = { onEvent(PokemonListEvent.SelectPokemon(it)) },
                onLoadMore = { onEvent(PokemonListEvent.LoadMorePokemons) },
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
private fun PokemonGrid(
    pokemons: List<PokemonSummary>,
    isLoadingMore: Boolean,
    onPokemonClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 4
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        items(pokemons, key = { it.id }) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) },
            )
        }
        if (isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: PokemonSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = pokemonColorNameToColor(pokemon.colorName)
    val contentColor = backgroundColor.contentColorFor()

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.aspectRatio(1f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Text(
                text = "${pokemon.id}",
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                modifier = Modifier.align(Alignment.TopStart),
            )
            SubcomposeAsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.Center),
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> CircularProgressIndicator(
                        color = contentColor,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(32.dp),
                    )
                    else -> SubcomposeAsyncImageContent()
                }
            }
            Text(
                text = pokemon.name.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            )
        }
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
    onRetry: () -> Unit,
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
        Button(onClick = onRetry) { Text("Retry") }
    }
}
