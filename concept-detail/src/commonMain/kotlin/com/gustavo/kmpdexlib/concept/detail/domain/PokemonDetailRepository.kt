package com.gustavo.kmpdexlib.concept.detail.domain

import com.gustavo.kmpdexlib.core.domain.DomainResult

interface PokemonDetailRepository {
    suspend fun getPokemonDetail(id: Int): DomainResult<PokemonDetail>
}
