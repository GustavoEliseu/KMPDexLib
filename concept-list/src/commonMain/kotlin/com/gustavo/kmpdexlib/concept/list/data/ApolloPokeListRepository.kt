package com.gustavo.kmpdexlib.concept.list.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.gustavo.kmpdexlib.concept.list.domain.PokemonListRepository
import com.gustavo.kmpdexlib.concept.list.domain.PokemonSummary
import com.gustavo.kmpdexlib.core.domain.DomainResult
import com.gustavo.kmpdexlib.data.graphql.GetPokemonListQuery

class ApolloPokeListRepository(
    private val apolloClient: ApolloClient,
) : PokemonListRepository {

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): DomainResult<List<PokemonSummary>> = try {
        val response = apolloClient
            .query(
                GetPokemonListQuery(
                    limit = Optional.present(limit),
                    offset = Optional.present(offset),
                )
            )
            .execute()
        val pokemons = response.data?.pokemon
            ?.map { it.toPokemonSummary() }
            ?: emptyList()
        DomainResult.Success(pokemons)
    } catch (e: ApolloException) {
        DomainResult.Error(e.message ?: "Failed to load Pokémon list", e)
    }
}
