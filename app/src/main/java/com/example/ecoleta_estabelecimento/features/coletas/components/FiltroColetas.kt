package com.example.ecoleta_estabelecimento.features.coletas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardVerdeClaro
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoFiltroTexto
import com.example.ecoleta_estabelecimento.ui.theme.Transparente

@Composable
fun FiltroColetas(
    filtroSelecionado: String,
    onFiltroSelecionado: (String) -> Unit
) {
    val filtros = listOf(
        "Todas",
        "Pendentes",
        "Aceitas"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(HomeCardVerdeClaro)
            .border(
                width = 1.dp,
                color = OfertaAceitaFundo,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        filtros.forEach { filtro ->
            val selecionado = filtro == filtroSelecionado

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        if (selecionado) Branco else Transparente
                    )
                    .clickable {
                        onFiltroSelecionado(filtro)
                    }
                    .padding(
                        horizontal = 5.dp,
                        vertical = 10.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filtro,
                    fontSize = 15.sp,
                    fontWeight = if (selecionado) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    color = if (selecionado) {
                        ResiduoFiltroTexto
                    } else {
                        HomeTextoSecundario
                    }
                )
            }
        }
    }
}