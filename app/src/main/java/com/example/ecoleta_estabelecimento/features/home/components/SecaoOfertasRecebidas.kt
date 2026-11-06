package com.example.ecoleta_estabelecimento.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun SecaoOfertasRecebidas(
    ofertas: List<OfertaResumo>,
    onVerTodas: () -> Unit = {},
    onVerOferta: (Int) -> Unit = {}
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        // Identificação da seção
        Text(
            text = "MOVIMENTAÇÕES RECENTES",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = VerdePrincipal
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Título e ação para ver todas
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ofertas recebidas",
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
                color = VerdeEscuro
            )

            TextButton(
                onClick = onVerTodas,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "VER TODAS →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrincipal
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Exibe no máximo três ofertas
        // Limita a exibição às três primeiras ofertas
        val ofertasRecentes = ofertas.take(3)

        ofertasRecentes.forEachIndexed { index, oferta ->

            CardOfertaRecebida(
                oferta = oferta,
                onVerOferta = onVerOferta
            )

            if (index < ofertasRecentes.lastIndex) {
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}