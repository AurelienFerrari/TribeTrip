package org.example.project.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Composable
expect fun interFontFamily(): FontFamily

val AppTypography: Typography
    @Composable
    get() {
        val interFamily = interFontFamily()
        val baseline = Typography()
        
        return Typography(
            displayLarge = baseline.displayLarge.copy(fontFamily = interFamily),
            displayMedium = baseline.displayMedium.copy(fontFamily = interFamily),
            displaySmall = baseline.displaySmall.copy(fontFamily = interFamily),
            headlineLarge = baseline.headlineLarge.copy(fontFamily = interFamily),
            headlineMedium = baseline.headlineMedium.copy(fontFamily = interFamily),
            headlineSmall = baseline.headlineSmall.copy(fontFamily = interFamily),
            titleLarge = baseline.titleLarge.copy(fontFamily = interFamily),
            titleMedium = baseline.titleMedium.copy(fontFamily = interFamily),
            titleSmall = baseline.titleSmall.copy(fontFamily = interFamily),
            bodyLarge = baseline.bodyLarge.copy(fontFamily = interFamily),
            bodyMedium = baseline.bodyMedium.copy(fontFamily = interFamily),
            bodySmall = baseline.bodySmall.copy(fontFamily = interFamily),
            labelLarge = baseline.labelLarge.copy(fontFamily = interFamily),
            labelMedium = baseline.labelMedium.copy(fontFamily = interFamily),
            labelSmall = baseline.labelSmall.copy(fontFamily = interFamily),
        )
    }

