package com.gustavo.kmpdexlib.concept.list.di

import com.gustavo.kmpdexlib.concept.list.data.ApolloPokeListRepository
import com.gustavo.kmpdexlib.concept.list.domain.GetPokemonListUseCase
import com.gustavo.kmpdexlib.concept.list.domain.PokemonListRepository
import com.gustavo.kmpdexlib.concept.list.features.list.PokemonListViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listModule = module {
    single<PokemonListRepository> { ApolloPokeListRepository(get()) }
    factory { GetPokemonListUseCase(get()) }
    viewModel { PokemonListViewModel(get()) }
}
