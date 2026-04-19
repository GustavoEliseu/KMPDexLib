package com.gustavo.kmpdexlib.concept.detail.di

import com.gustavo.kmpdexlib.concept.detail.data.ApolloPokeDetailRepository
import com.gustavo.kmpdexlib.concept.detail.domain.GetPokemonDetailUseCase
import com.gustavo.kmpdexlib.concept.detail.domain.PokemonDetailRepository
import com.gustavo.kmpdexlib.concept.detail.features.detail.PokemonDetailViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    single<PokemonDetailRepository> { ApolloPokeDetailRepository(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    viewModel { PokemonDetailViewModel(get()) }
}
