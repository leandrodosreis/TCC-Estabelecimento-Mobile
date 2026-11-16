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
import com.example.ecoleta_estabelecimento.features.coletas.model.Coleta
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaBotaoBorda
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaTextoDetalhe
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import java.text.NumberFormat
import java.util.Locale

// Formatação da quantidade
private fun formatarQuantidadeColeta(
    quantidade: String
): String {
    val texto = quantidade.trim()

    if (texto.endsWith("kg", ignoreCase = true)) {
        return texto
    }

    val numero = texto.replace(',', '.')
        .toBigDecimalOrNull()
        ?: return texto

    return "${numero.stripTrailingZeros().toPlainString().replace('.', ',')} kg"
}

// Formatação do valor ofertado
private fun formatarValorColeta(
    valor: String
): String {
    val texto = valor.trim()

    if (texto.startsWith("R$")) {
        return texto
    }

    val numero = texto.replace(',', '.')
        .toBigDecimalOrNull()
        ?: return texto

    val formato = NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("pt-BR")
    )

    return formato.format(numero)
}

// Card para exibir as ofertas
@Composable
fun CardColeta(
    coleta: Coleta,
    onVerMais: () -> Unit
) {
    // Verifica o status retornado pela API
    val pendente = coleta.statusOferta
        .trim()
        .equals("pendente", ignoreCase = true)

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

    val quantidadeFormatada = formatarQuantidadeColeta(coleta.quantidade)

    val valorFormatado = formatarValorColeta(coleta.valorOfertado)

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
            painter = painterResource(R.drawable.icone_coletas_card),
            contentDescription = "Empresa coletora",
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Empresa e dados do resíduo
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coleta.nomeEmpresaColetora,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = nomeTela
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${coleta.material} - $quantidadeFormatada",
                fontSize = 15.sp,
                color = subTitulo
            )

            Text(
                text = coleta.dataDisponivel,
                fontSize = 13.sp,
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
                text = valorFormatado,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = nomeTela
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Status e botão ver mais
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
                    text = if (pendente) "PENDENTE" else "ACEITA",
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
                border = BorderStroke(
                    width = 1.dp,
                    color = OfertaBotaoBorda
                ),
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