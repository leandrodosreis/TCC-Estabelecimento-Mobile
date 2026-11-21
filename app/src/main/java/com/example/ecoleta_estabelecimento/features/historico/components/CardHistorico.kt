package com.example.ecoleta_estabelecimento.features.historico.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HistoricoCanceladoFundo
import com.example.ecoleta_estabelecimento.ui.theme.HistoricoCanceladoTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaBotaoBorda
import com.example.ecoleta_estabelecimento.ui.theme.OfertaTextoDetalhe
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

// =====================
// Tipos de histórico
// =====================

enum class TipoHistorico {
    CONCLUIDA,
    CANCELADO
}

// Card do histórico
@Composable
fun CardHistorico(
    titulo: String,
    material: String,
    quantidade: String,
    data: String,
    tipo: TipoHistorico,
    valor: String? = null,
    onVerMais: () -> Unit
) {

    // Define as cores conforme o tipo do registro
    val statusFundo = when (tipo) {
        TipoHistorico.CONCLUIDA -> OfertaAceitaFundo
        TipoHistorico.CANCELADO -> HistoricoCanceladoFundo
    }

    val statusTexto = when (tipo) {
        TipoHistorico.CONCLUIDA -> OfertaAceitaTexto
        TipoHistorico.CANCELADO -> HistoricoCanceladoTexto
    }

    val statusRotulo = when (tipo) {
        TipoHistorico.CONCLUIDA -> "CONCLUÍDA"
        TipoHistorico.CANCELADO -> "CANCELADO"
    }

    // Estrutura principal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Branco)
            .heightIn(min = 130.dp)
            .padding(
                horizontal = 12.dp,
                vertical = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Ícone do registro
        Image(
            painter = painterResource(R.drawable.icone_coletas_card),
            contentDescription = if (tipo == TipoHistorico.CONCLUIDA) {
                "Coleta concluída"
            } else {
                "Resíduo cancelado"
            },
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Informações
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = nomeTela
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "$material - $quantidade",
                fontSize = 15.sp,
                color = subTitulo
            )

            Text(
                text = data,
                fontSize = 13.sp,
                color = subTitulo
            )

            if (tipo == TipoHistorico.CONCLUIDA && !valor.isNullOrBlank()) {

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Valor acordado",
                    fontSize = 12.sp,
                    color = OfertaTextoDetalhe
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = valor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = nomeTela
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status e botão
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .width(95.dp)
                    .height(35.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusFundo),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = statusRotulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = statusTexto,
                    maxLines = 1,
                    softWrap = false
                )
            }

            OutlinedButton(
                onClick = onVerMais,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, OfertaBotaoBorda),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Branco,
                    contentColor = nomeTela
                ),
                contentPadding = PaddingValues(
                    horizontal = 4.dp,
                    vertical = 0.dp
                ),
                modifier = Modifier
                    .width(95.dp)
                    .height(40.dp)
            ) {

                Text(
                    text = "Ver mais",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}