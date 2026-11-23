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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HistoricoCanceladoFundo
import com.example.ecoleta_estabelecimento.ui.theme.HistoricoCanceladoTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaBotaoBorda
import com.example.ecoleta_estabelecimento.ui.theme.OfertaTextoDetalhe
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

enum class TipoHistorico {
    CONCLUIDA,
    CANCELADO
}

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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        border = BorderStroke(1.dp, BordaPadrao)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. TOPO: Ícone, Título e Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(R.drawable.icone_coletas_card),
                        contentDescription = if (tipo == TipoHistorico.CONCLUIDA) {
                            "Coleta concluída"
                        } else {
                            "Resíduo cancelado"
                        },
                        modifier = Modifier.size(45.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = titulo,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = nomeTela,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

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
            }

            HorizontalDivider(color = BordaPadrao, thickness = 1.dp)

            // 2. MEIO: 3 Colunas (Material, Quantidade, Data)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Material",
                        fontSize = 12.sp,
                        color = subTitulo
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = material,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = nomeTela,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Quantidade",
                        fontSize = 12.sp,
                        color = subTitulo
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "$quantidade kg",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = nomeTela,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Data",
                        fontSize = 12.sp,
                        color = subTitulo
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = data,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = nomeTela,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            HorizontalDivider(color = BordaPadrao, thickness = 1.dp)

            // 3. RODAPÉ: "Valor acordado" e o valor lado a lado na mesma linha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (tipo == TipoHistorico.CONCLUIDA && !valor.isNullOrBlank()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = "Valor acordado:",
                            fontSize = 13.sp,
                            color = OfertaTextoDetalhe
                        )
                        Text(
                            text = if (valor.startsWith("R$")) {
                                valor
                            } else {
                                "R$ $valor"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = nomeTela
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                OutlinedButton(
                    onClick = onVerMais,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, OfertaBotaoBorda),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Branco,
                        contentColor = nomeTela
                    ),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
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
}