package com.gustavo.kmpdexlib.concept.list.features.list

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

// Colors adapted from MyPokedexApp's ColorEnum tintColors.
// The API already returns the color name, so there's no need for
// Android-only Palette extraction from the sprite bitmap.
fun pokemonColorNameToColor(colorName: String): Color = when (colorName.lowercase()) {
    "black"  -> Color(90,  90,  88)   // softened black — pure black is too harsh as a card bg
    "blue"   -> Color(53,  91,  245)
    "brown"  -> Color(135, 99,  58)
    "gray"   -> Color(160, 162, 165)
    "green"  -> Color(142, 211, 153)
    "pink"   -> Color(251, 202, 224)
    "purple" -> Color(90,  36,  123)
    "red"    -> Color(189, 39,  52)
    "white"  -> Color(220, 220, 220)  // off-white — pure white disappears on a white background
    "yellow" -> Color(229, 183, 35)
    else     -> Color(160, 162, 165)
}

// Mirrors MyPokedexApp's isDarkColor() logic, adapted to KMP-compatible Compose API.
fun Color.contentColorFor(): Color =
    if (luminance() < 0.4f) Color.White else Color(20, 20, 20)
