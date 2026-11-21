package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun CardResumos(
    modifier: Modifier = Modifier,
    nomeEmpresa: String,
    cnpjEmpresa: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Image(
                painter = painterResource(R.drawable.icone_perfil),
                contentDescription = "Estabelecimento",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Nome e CNPJ do estabelecimento
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = nomeEmpresa,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerdeEscuro
                )

                Text(
                    text = cnpjEmpresa,
                    fontSize = 13.sp,
                    color = subTitulo
                )
            }
        }
    }
}