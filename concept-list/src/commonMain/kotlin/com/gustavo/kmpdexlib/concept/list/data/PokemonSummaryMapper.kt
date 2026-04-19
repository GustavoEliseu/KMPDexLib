package com.gustavo.kmpdexlib.concept.list.data

import com.gustavo.kmpdexlib.concept.list.domain.PokemonSummary
import com.gustavo.kmpdexlib.data.graphql.GetPokemonListQuery

fun GetPokemonListQuery.Pokemon.toPokemonSummary(): PokemonSummary = PokemonSummary(
    id = id,
    name = name,
    colorName = pokemonspecy?.pokemoncolor?.name ?: "gray",
    spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
)
