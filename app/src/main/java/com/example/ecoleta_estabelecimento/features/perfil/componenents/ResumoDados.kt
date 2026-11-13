package com.example.ecoleta_estabelecimento.features.perfil.componenents

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Cinza
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase

@Composable
fun CardResumos(
    nomeEmpresa: String,
    cnpjEmpresa: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White

        )
        ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icone_perfil),
                    contentDescription = "Imagem de perfil",
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = nomeEmpresa,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = CorTextoBase
                    )

                    Text(
                        text = cnpjEmpresa,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = Cinza
                    )
                }
            }

            }
        }

    }