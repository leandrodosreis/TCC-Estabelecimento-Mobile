package com.example.ecoleta_estabelecimento.components.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

object CarregamentoEcoleta {

    @Composable
    fun Pontinhos() {
        // Cria um controlador de animações que podem se repetir infinitamente
        val transicao = rememberInfiniteTransition(
            label = "pontinhos"
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { indice ->
                // Cria cada pontinho
                    // Anima um número decimal (Float) entre dois valores
                val escala = transicao.animateFloat(
                    // Tamanho das bolinhas, que aumentam e diminuem
                    initialValue = 0.6f,
                    targetValue = 1f,
                    // Cria o efeito de movimento em sequência, em vez de todos pulsarem exatamente ao mesmo tempo
                    animationSpec = infiniteRepeatable(
                        // Define a duração e o atraso da animação
                        animation = tween(
                            durationMillis = 450,
                            delayMillis = indice * 150
                        ),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "ponto$indice"
                )

                // Desenha cada bolinha
                Box(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                        .size((4 * escala.value).dp)
                        .background(VerdePrincipal, CircleShape)
                )
            }
        }
    }

    @Composable
    fun Circular() {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = VerdePrincipal,
            // Define a espessura do traço
            strokeWidth = 3.dp,
            // Define a cor do círculo de fundo
            trackColor = VerdePrincipal.copy(alpha = 0.15f)
        )
    }
}