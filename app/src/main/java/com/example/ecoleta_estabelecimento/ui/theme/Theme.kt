package com.example.ecoleta_estabelecimento.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define as cores principais do Ecoleta
private val EcoletaColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    background = VerdeFundo,
    surface = VerdeFundo,
    onBackground = VerdeEscuro,
    onSurface = VerdeEscuro
)

@Composable
fun EcoletaEstabelecimentoTheme(
    content: @Composable () -> Unit
) {

    // Aplica as cores e a tipografia do aplicativo
    MaterialTheme(
        colorScheme = EcoletaColorScheme,
        typography = Typography,
        content = content
    )
}