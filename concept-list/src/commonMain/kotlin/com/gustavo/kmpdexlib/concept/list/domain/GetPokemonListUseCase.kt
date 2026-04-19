package com.gustavo.kmpdexlib.concept.list.domain

import com.gustavo.kmpdexlib.core.domain.DomainResult

class GetPokemonListUseCase(
    private val repository: PokemonListRepository,
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
    ): DomainResult<List<PokemonSummary>> = repository.getPokemonList(limit, offset)
}
