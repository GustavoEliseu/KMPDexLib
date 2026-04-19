package com.gustavo.kmpdexlib.concept.detail.domain

import com.gustavo.kmpdexlib.core.domain.DomainResult

class GetPokemonDetailUseCase(
    private val repository: PokemonDetailRepository,
) {
    suspend operator fun invoke(id: Int): DomainResult<PokemonDetail> =
        repository.getPokemonDetail(id)
}
