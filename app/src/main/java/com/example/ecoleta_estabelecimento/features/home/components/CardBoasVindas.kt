package com.example.ecoleta_estabelecimento.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun CardBoasVindas() {

    // Card principal de boas-vindas
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Branco
        ),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // Indicador de operação sustentável
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.operacao_sustentavel),
                    contentDescription = "Operação sustentável",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Operação sustentável",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VerdePrincipal
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Saudação
            Text(
                text = "Olá, Restaurante X.",
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
                color = VerdeEscuro
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Descrição
            Text(
                text = "Pequenas ações geram um impacto que podemos medir.",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                color = HomeTextoSecundario
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quantidade de materiais encaminhados
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.materiais_encaminhados),
                    contentDescription = "Quantidade em materiais encaminhados",
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = "73 kg",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeEscuro
                    )

                    Text(
                        text = "já encaminhados",
                        fontSize = 14.sp,
                        color = HomeTextoSecundario
                    )
                }
            }
        }
    }
}