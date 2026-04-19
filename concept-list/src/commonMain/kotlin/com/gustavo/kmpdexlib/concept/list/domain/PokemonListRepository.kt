package com.gustavo.kmpdexlib.concept.list.domain

import com.gustavo.kmpdexlib.core.domain.DomainResult

interface PokemonListRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): DomainResult<List<PokemonSummary>>
}
