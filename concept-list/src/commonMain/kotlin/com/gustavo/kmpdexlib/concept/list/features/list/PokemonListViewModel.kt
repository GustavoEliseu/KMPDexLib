package com.gustavo.kmpdexlib.concept.list.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavo.kmpdexlib.concept.list.domain.GetPokemonListUseCase
import com.gustavo.kmpdexlib.core.domain.DomainResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val getPokemonList: GetPokemonListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonListState())
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    private val _effects = Channel<PokemonListEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<PokemonListEffect> = _effects.receiveAsFlow()

    private var currentOffset = 0

    init {
        onEvent(PokemonListEvent.LoadPokemons)
    }

    fun onEvent(event: PokemonListEvent) {
        when (event) {
            PokemonListEvent.LoadPokemons -> loadPokemons()
            PokemonListEvent.LoadMorePokemons -> loadMorePokemons()
            is PokemonListEvent.SelectPokemon -> selectPokemon(event.id)
        }
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            currentOffset = 0
            updateState { copy(isLoading = true, error = null) }
            when (val result = getPokemonList(limit = PAGE_SIZE, offset = 0)) {
                is DomainResult.Success -> {
                    currentOffset = result.data.size
                    updateState {
                        copy(
                            isLoading = false,
                            pokemons = result.data,
                            canLoadMore = result.data.size >= PAGE_SIZE,
                        )
                    }
                }
                is DomainResult.Error -> updateState {
                    copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    private fun loadMorePokemons() {
        if (_state.value.isLoadingMore || !_state.value.canLoadMore) return
        viewModelScope.launch {
            updateState { copy(isLoadingMore = true) }
            when (val result = getPokemonList(limit = PAGE_SIZE, offset = currentOffset)) {
                is DomainResult.Success -> {
                    currentOffset += result.data.size
                    updateState {
                        copy(
                            isLoadingMore = false,
                            pokemons = pokemons + result.data,
                            canLoadMore = result.data.size >= PAGE_SIZE,
                        )
                    }
                }
                is DomainResult.Error -> updateState {
                    copy(isLoadingMore = false, error = result.message)
                }
            }
        }
    }

    private fun selectPokemon(id: Int) {
        viewModelScope.launch {
            _effects.send(PokemonListEffect.NavigateToDetail(id))
        }
    }

    private fun updateState(reducer: PokemonListState.() -> PokemonListState) {
        _state.update { it.reducer() }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
