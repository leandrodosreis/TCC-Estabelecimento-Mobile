package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun BotaoNovoResiduo(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VerdePrincipal,
            contentColor = Branco
        )
    ) {
        Text(
            text = "+ Novo Resíduo",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
    }
}