package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun MensagemListaVazia(
    titulo: String,
    descricao: String
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 45.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = subTitulo,
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = titulo,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = nomeTela,
            textAlign = TextAlign.Center
        )

        Text(
            text = descricao,
            fontSize = 13.sp,
            color = subTitulo,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}