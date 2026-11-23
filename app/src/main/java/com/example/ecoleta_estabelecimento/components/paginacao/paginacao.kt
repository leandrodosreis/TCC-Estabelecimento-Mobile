package com.example.ecoleta_estabelecimento.components.paginacao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.BotaoPaginacao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase
import com.example.ecoleta_estabelecimento.ui.theme.Paginacao
import com.example.ecoleta_estabelecimento.ui.theme.PaginacaoElse
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun ComponentePaginacao(
    paginaAtual: Int,
    totalPaginas: Int,
    onPaginaSelecionada: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botão Anterior (<)
        OutlinedButton(
            onClick = { if (paginaAtual > 1) onPaginaSelecionada(paginaAtual - 1) },
            enabled = paginaAtual > 1,
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (paginaAtual > 1) {
                    Paginacao
                } else {
                    PaginacaoElse
                }
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                disabledContainerColor = BotaoPaginacao
            )
        ) {
            Text(
                text = "<",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (paginaAtual > 1) {
                    CorTextoBase
                } else {
                    Color.LightGray
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Gerar os botões numéricos (1, 2, 3...)
        for (i in 1..totalPaginas) {
            val selecionada = i == paginaAtual

            OutlinedButton(
                onClick = {
                    onPaginaSelecionada(i)
                },
                modifier = Modifier
                    .size(46.dp)
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(0.dp),
                border = if (selecionada) {
                    null
                } else {
                    BorderStroke(1.dp, Color(0xFFD6E1DD))
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selecionada) {
                        VerdePrincipal
                    } else {
                        Branco
                    }
                )
            ) {
                Text(
                    text = i.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selecionada) {
                        Branco
                    } else {
                        CorTextoBase
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            onClick = {
                if (paginaAtual < totalPaginas) {
                    onPaginaSelecionada(paginaAtual + 1)
                }
            },
            enabled = paginaAtual < totalPaginas,
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (paginaAtual < totalPaginas) {
                    Color(0xFFD6E1DD)
                } else {
                    Color(0xFFE3E8E5)
                }
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                disabledContainerColor = Color(0xFFF9F9F9)
            )
        ) {
            Text(
                text = ">",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (paginaAtual < totalPaginas) {
                    CorTextoBase
                } else {
                    Color.LightGray
                }
            )
        }
    }
}