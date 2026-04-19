package com.gustavo.kmpdexlib

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gustavo.kmpdexlib.concept.detail.di.detailModule
import com.gustavo.kmpdexlib.concept.detail.features.detail.PokemonDetailScreen
import com.gustavo.kmpdexlib.concept.list.di.listModule
import com.gustavo.kmpdexlib.concept.list.features.list.PokemonListScreen
import com.gustavo.kmpdexlib.core.data.coreDataModule
import com.gustavo.kmpdexlib.core.infra.coreInfraModule
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(coreInfraModule, coreDataModule, listModule, detailModule)
    }) {
        MaterialTheme {
            var screen: AppScreen by remember { mutableStateOf(AppScreen.List) }

            when (val current = screen) {
                AppScreen.List -> PokemonListScreen(
                    onPokemonClick = { id -> screen = AppScreen.Detail(id) },
                )
                is AppScreen.Detail -> PokemonDetailScreen(
                    pokemonId = current.id,
                    onBack = { screen = AppScreen.List },
                )
            }
        }
    }
}

sealed interface AppScreen {
    data object List : AppScreen
    data class Detail(val id: Int) : AppScreen
}
