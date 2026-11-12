package com.example.ecoleta_estabelecimento.features.perfil.componenents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.Cinza
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro

@Composable
fun CardEndereco(
    modifier: Modifier = Modifier,
    nomeEmpresa: String,
    cnpj: String,
    telefone: String,
    email: String
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Branco
        ),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = nomeEmpresa,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeEscuro
            )

            Text(
                text = cnpj,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = HomeTextoSecundario
            )

            Text(
                text = telefone,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = HomeTextoSecundario
            )

            Text(
                text = email,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = HomeTextoSecundario
            )
        }
    }
}