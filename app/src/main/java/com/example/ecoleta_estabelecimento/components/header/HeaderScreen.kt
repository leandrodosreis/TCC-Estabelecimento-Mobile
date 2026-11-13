package com.example.ecoleta_estabelecimento.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HeaderSombra
import com.example.ecoleta_estabelecimento.ui.theme.HeaderTexto
import com.example.ecoleta_estabelecimento.ui.theme.Transparente

@Composable
fun HeaderScreen(
    modifier: Modifier = Modifier,
    rotaAtual: String = "home",
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {

    // Organiza o Header
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Área branca do cabeçalho
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Branco,
            shadowElevation = 0.dp
        ) {
            // Logo à esquerda e botão Sair à direita
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo que leva para Home
                Image(
                    painter = painterResource(
                        id = R.drawable.logo_ecoleta
                    ),
                    contentDescription = "Voltar para Home",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(113.dp)
                        .height(30.dp)
                        .clickable {
                            onNavigate("home")
                        }
                )

                // Botão Sair
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onLogout()
                        }
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícone de saída personalizado
                    Image(
                        painter = painterResource(
                            id = R.drawable.icone_sair
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(22.dp)
                    )

                    // Texto do botão
                    Text(
                        text = "Sair",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = HeaderTexto
                    )
                }
            }
        }

        // Mantém a sombra original abaixo do Header
        Box(
            modifier = modifier.fillMaxWidth()
                .height(5.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            HeaderSombra,
                            Transparente
                        )
                    )
                )
        )
    }
}