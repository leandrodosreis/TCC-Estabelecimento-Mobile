package com.example.ecoleta_estabelecimento.features.home.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.HomeIconeFundo
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro

@Composable
fun CardResumo(
    modifier: Modifier = Modifier,
    valor: String,
    descricao: String,
    icone: Int,
    fundo: Color = Branco
) {

    // Card reutilizável dos indicadores
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = fundo
        ),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {

        // Organiza ícone e textos lado a lado
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(icone),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            // Valor e descrição do indicador
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = valor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeEscuro
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = descricao,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = HomeTextoSecundario
                )
            }
        }
    }
}