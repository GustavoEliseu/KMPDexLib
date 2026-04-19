package com.gustavo.kmpdexlib.concept.detail.data

import com.gustavo.kmpdexlib.concept.detail.domain.PokemonDetail
import com.gustavo.kmpdexlib.data.graphql.GetPokemonDetailQuery

fun GetPokemonDetailQuery.Pokemon.toPokemonDetail(): PokemonDetail = PokemonDetail(
    id = id,
    name = name,
    colorName = pokemonspecy?.pokemoncolor?.name ?: "gray",
    description = pokemonspecy
        ?.pokemonspeciesflavortexts
        ?.firstOrNull()
        ?.flavor_text
        ?.cleanFlavorText()
        ?: "",
    spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
    types = pokemontypes.mapNotNull { it.type?.name },
)

private fun String.cleanFlavorText(): String =
    replace("\n", " ").replace("\u000C", " ").trim()
