package com.example.ecoleta_estabelecimento.features.coletas.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.features.coletas.ColetaExemplo
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaBotaoBorda
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaTextoDetalhe
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun CardColeta(
    coleta: ColetaExemplo,
    onVerMais: () -> Unit
) {
    val pendente = coleta.status == "pendente"

    val statusFundo = if (pendente) {
        OfertaPendenteFundo
    } else {
        OfertaAceitaFundo
    }

    val statusTexto = if (pendente) {
        OfertaPendenteTexto
    } else {
        OfertaAceitaTexto
    }

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
        Image(
            painter = painterResource(
                id = R.drawable.icone_coletas_card
            ),
            contentDescription = "Empresa coletora",
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Empresa e dados do resíduo
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coleta.empresa,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = nomeTela
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${coleta.material} - ${coleta.quantidade} - ${coleta.distancia}",
                fontSize = 15.sp,
                color = subTitulo
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Oferta enviada",
                fontSize = 12.sp,
                color = OfertaTextoDetalhe
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = coleta.valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = nomeTela
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

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
                    text = if (pendente) {
                        "PENDENTE"
                    } else {
                        "ACEITA"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusTexto,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(statusFundo)
                        .padding(
                            horizontal = 14.dp,
                            vertical = 5.dp
                        )
                )
            }

            OutlinedButton(
                onClick = onVerMais,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = OfertaBotaoBorda
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Branco,
                    contentColor = nomeTela
                ),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 5.dp
                ),
                modifier = Modifier.height(40.dp)
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