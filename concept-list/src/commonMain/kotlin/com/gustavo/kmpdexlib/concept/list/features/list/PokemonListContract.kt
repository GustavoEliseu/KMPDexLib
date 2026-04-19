package com.gustavo.kmpdexlib.concept.list.features.list

import androidx.compose.runtime.Immutable
import com.gustavo.kmpdexlib.concept.list.domain.PokemonSummary

@Immutable
data class PokemonListState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val pokemons: List<PokemonSummary> = emptyList(),
    val error: String? = null,
)

sealed interface PokemonListEvent {
    data object LoadPokemons : PokemonListEvent
    data object LoadMorePokemons : PokemonListEvent
    data class SelectPokemon(val id: Int) : PokemonListEvent
}

sealed interface PokemonListEffect {
    data class NavigateToDetail(val id: Int) : PokemonListEffect
}
