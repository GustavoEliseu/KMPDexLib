package com.gustavo.kmpdexlib.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.gustavo.kmpdexlib.concept.detail.di.detailModule
import com.gustavo.kmpdexlib.concept.detail.features.detail.PokemonDetailScreen
import com.gustavo.kmpdexlib.concept.list.di.listModule
import com.gustavo.kmpdexlib.concept.list.features.list.PokemonListScreen
import com.gustavo.kmpdexlib.core.data.coreDataModule
import com.gustavo.kmpdexlib.core.infra.coreInfraModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

/**
 * Call once in AppDelegate.application(_:didFinishLaunchingWithOptions:) before
 * presenting any KMPDexLib view controller.
 */
fun startKMPDexLib() {
    startKoin {
        modules(
            coreInfraModule,
            coreDataModule,
            listModule,
            detailModule,
        )
    }
}

/**
 * Returns a UIViewController that renders the Pokémon list screen.
 *
 * @param onPokemonClick called with the selected Pokémon's id when the user taps a card.
 */
fun pokemonListViewController(onPokemonClick: (Int) -> Unit): UIViewController =
    ComposeUIViewController {
        PokemonListScreen(onPokemonClick = onPokemonClick)
    }

/**
 * Returns a UIViewController that renders the Pokémon detail screen.
 *
 * @param pokemonId the id of the Pokémon to display.
 * @param onBack called when the user taps the back button.
 */
fun pokemonDetailViewController(pokemonId: Int, onBack: () -> Unit): UIViewController =
    ComposeUIViewController {
        PokemonDetailScreen(pokemonId = pokemonId, onBack = onBack)
    }
