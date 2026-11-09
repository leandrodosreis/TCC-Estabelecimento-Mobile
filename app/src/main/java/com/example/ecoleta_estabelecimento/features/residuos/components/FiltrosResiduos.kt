package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoFiltroTexto
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun FiltrosResiduos() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Filtro por tipo de material
        OutlinedButton(
            onClick = { },
            modifier = Modifier.weight(1f).height(60.dp),
            shape = RoundedCornerShape(17.dp),
            border = BorderStroke(1.dp, BordaPadrao),
            contentPadding = PaddingValues(horizontal = 4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                contentColor = ResiduoFiltroTexto
            )
        ) {
            Text(
                text = "Tipos",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )

            // Espaçamento entre o texto e a seta
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Selecionar tipo",
                tint = subTitulo,
                modifier = Modifier.size(26.dp)
            )
        }

        // Filtro por status
        OutlinedButton(
            onClick = { },
            modifier = Modifier.weight(1f).height(60.dp),
            shape = RoundedCornerShape(17.dp),
            border = BorderStroke(1.dp, BordaPadrao),
            contentPadding = PaddingValues(horizontal = 4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                contentColor = ResiduoFiltroTexto
            )
        ) {
            Text(
                text = "Status",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )

            // Espaçamento entre o texto e a seta
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Selecionar status",
                tint = subTitulo,
                modifier = Modifier.size(26.dp)
            )
        }

        // Botão para aplicar os filtros
        OutlinedButton(
            onClick = { },
            modifier = Modifier.weight(1f).height(60.dp),
            shape = RoundedCornerShape(17.dp),
            border = BorderStroke(1.dp, BordaPadrao),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                contentColor = ResiduoFiltroTexto
            )
        ) {
            Text(
                text = "Filtrar",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}