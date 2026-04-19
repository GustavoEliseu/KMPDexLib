package com.gustavo.kmpdexlib.concept.detail.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.gustavo.kmpdexlib.concept.detail.domain.PokemonDetail
import com.gustavo.kmpdexlib.concept.detail.domain.PokemonDetailRepository
import com.gustavo.kmpdexlib.core.domain.DomainResult
import com.gustavo.kmpdexlib.data.graphql.GetPokemonDetailQuery

class ApolloPokeDetailRepository(
    private val apolloClient: ApolloClient,
) : PokemonDetailRepository {

    override suspend fun getPokemonDetail(id: Int): DomainResult<PokemonDetail> = try {
        val response = apolloClient
            .query(GetPokemonDetailQuery(id = id))
            .execute()
        val pokemon = response.data?.pokemon?.firstOrNull()
            ?: return DomainResult.Error("Pokémon #$id not found")
        DomainResult.Success(pokemon.toPokemonDetail())
    } catch (e: ApolloException) {
        DomainResult.Error(e.message ?: "Failed to load Pokémon detail", e)
    }
}
