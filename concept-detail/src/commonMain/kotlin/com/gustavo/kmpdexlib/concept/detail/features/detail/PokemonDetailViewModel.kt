package com.gustavo.kmpdexlib.concept.detail.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavo.kmpdexlib.concept.detail.domain.GetPokemonDetailUseCase
import com.gustavo.kmpdexlib.core.domain.DomainResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val getPokemonDetail: GetPokemonDetailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonDetailState())
    val state: StateFlow<PokemonDetailState> = _state.asStateFlow()

    private val _effects = Channel<PokemonDetailEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<PokemonDetailEffect> = _effects.receiveAsFlow()

    fun onEvent(event: PokemonDetailEvent) {
        when (event) {
            is PokemonDetailEvent.LoadPokemon -> loadPokemon(event.id)
        }
    }

    private fun loadPokemon(id: Int) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            when (val result = getPokemonDetail(id)) {
                is DomainResult.Success -> updateState {
                    copy(isLoading = false, pokemon = result.data)
                }
                is DomainResult.Error -> updateState {
                    copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    private fun updateState(reducer: PokemonDetailState.() -> PokemonDetailState) {
        _state.update { it.reducer() }
    }
}
