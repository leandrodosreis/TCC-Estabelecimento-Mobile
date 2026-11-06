package com.example.ecoleta_estabelecimento.components.footer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.FooterLinha
import com.example.ecoleta_estabelecimento.ui.theme.FooterTexto
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo

@Composable
fun FooterScreen(modifier: Modifier = Modifier) {

    // Define as cores do Footer
    val fundo = Color(0xFFF0F9F4)
    val corTexto = Color(0xFF9AA8A0)
    val corLinha = Color(0xFFD3DED7)

    // Organiza a linha e os textos verticalmente
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VerdeFundo)
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp
            )
    ) {
        // Cria a linha divisória superior
        HorizontalDivider(
            thickness = 1.dp,
            color = FooterLinha
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exibe a mensagem do Ecoleta
            Text(
                text = "Conectando materiais a novos destinos.",
                fontSize = 11.sp,
                color = FooterTexto
            )

            // Exibe os direitos autorais
            Text(
                text = "© 2026 Ecoleta",
                fontSize = 11.sp,
                color = FooterTexto
            )
        }
    }
}