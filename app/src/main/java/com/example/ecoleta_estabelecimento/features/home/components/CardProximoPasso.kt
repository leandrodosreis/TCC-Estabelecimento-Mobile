package com.example.ecoleta_estabelecimento.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardEscuro
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoDestaque
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoEscuroSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro

@Composable
fun CardProximoPasso(
    onCadastrarResiduo: () -> Unit = {}
) {
    // Card com ação principal da Home
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = HomeCardEscuro
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Identificação da próxima ação
            Text(
                text = "PRÓXIMO PASSO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = HomeTextoDestaque
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Título
            Text(
                text = "Disponibilize novo resíduo",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = Branco
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descrição
            Text(
                text = "Cadastre um material para receber ofertas de coleta.",
                fontSize = 13.sp,
                color = HomeTextoEscuroSecundario
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Botão de cadastrar resíduo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = Branco,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onCadastrarResiduo()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cadastrar resíduo    →",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VerdeEscuro
                )
            }
        }
    }
}