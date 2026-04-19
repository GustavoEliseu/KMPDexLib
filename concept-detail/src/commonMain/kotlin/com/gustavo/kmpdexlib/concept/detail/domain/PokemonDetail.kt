package com.gustavo.kmpdexlib.concept.detail.domain

data class PokemonDetail(
    val id: Int,
    val name: String,
    val colorName: String,
    val description: String,
    val spriteUrl: String,
    val types: List<String>,
)
