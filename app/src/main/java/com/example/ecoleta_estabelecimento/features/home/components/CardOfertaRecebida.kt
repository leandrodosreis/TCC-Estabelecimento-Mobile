package com.example.ecoleta_estabelecimento.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaBotaoBorda
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaTextoDetalhe
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CardOfertaRecebida(
    oferta: OfertaResumo,
    onVerOferta: (Int) -> Unit = {}
) {
    val aceita = oferta.status.equals(
        "ACEITA",
        ignoreCase = true
    )

    val corStatusFundo = if (aceita) {
        OfertaAceitaFundo
    } else {
        OfertaPendenteFundo
    }

    val corStatusTexto = if (aceita) {
        OfertaAceitaTexto
    } else {
        OfertaPendenteTexto
    }

    val valorFormatado = NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
        .format(oferta.valor)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Branco
        ),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {
        Column (
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nome da empresa e status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = oferta.nomeColetor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerdeEscuro,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = corStatusFundo
                ) {
                    Text(
                        text = oferta.status.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = corStatusTexto,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 9.dp
                        )
                    )
                }
            }

            // Material, quantidade e data
            Text(
                text = "${oferta.material}  -  ${oferta.quantidadeKg} kg  -  ${oferta.data}",
                fontSize = 14.sp,
                color = OfertaTextoDetalhe
            )

            // Valor oferecido e botão
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Valor ofertado",
                        fontSize = 13.sp,
                        color = OfertaTextoDetalhe
                    )

                    Text(
                        text = valorFormatado,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeEscuro
                    )
                }

                OutlinedButton (
                    onClick = { onVerOferta(oferta.id) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, OfertaBotaoBorda),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
                ) {
                    Text(
                        text = if (aceita) "Ver coleta" else "Ver oferta",
                        fontSize = 12.sp,
                        color = VerdeEscuro
                    )
                }
            }
        }
    }
}