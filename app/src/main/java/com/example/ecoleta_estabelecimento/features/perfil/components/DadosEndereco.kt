package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel



    @Composable
    fun CardEndereco(
        modifier: Modifier = Modifier,
        logradouro: String,
        bairro: String,
        cidade: String,
        estado: String,
        cep: String
    ) {

        var logradouro by remember {
            mutableStateOf(logradouro)
        }

        var bairro by remember {
            mutableStateOf(bairro)
        }

        var cidade by remember {
            mutableStateOf(cidade)
        }

        var estado by remember {
            mutableStateOf(estado)
        }

        var cep by remember {
            mutableStateOf(cep)
        }

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
                    text = "ENDEREÇO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = textoPainel
                )
            }
        }


    }