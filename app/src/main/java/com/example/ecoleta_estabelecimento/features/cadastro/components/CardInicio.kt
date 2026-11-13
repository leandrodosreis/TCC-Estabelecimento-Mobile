package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardInicial(
){
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .offset(y = (-20).dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF9F6F6)
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Text(
                text = "CADASTRE-SE",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            )

            Text(
                text = "Disponibilize os resíduos para melhorar o meio ambiente, através da Ecoleta",
                modifier = Modifier. padding(top = 20.dp, start = 30.dp, end = 10.dp)
            )

        }
    }

}