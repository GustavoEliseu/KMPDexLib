package com.gustavo.kmpdexlib.concept.detail.features.detail

import androidx.compose.runtime.Immutable
import com.gustavo.kmpdexlib.concept.detail.domain.PokemonDetail

@Immutable
data class PokemonDetailState(
    val isLoading: Boolean = true,
    val pokemon: PokemonDetail? = null,
    val error: String? = null,
)

sealed interface PokemonDetailEvent {
    data class LoadPokemon(val id: Int) : PokemonDetailEvent
}

sealed interface PokemonDetailEffect {
    data object NavigateBack : PokemonDetailEffect
}
