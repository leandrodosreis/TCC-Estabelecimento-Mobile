package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.OfertaBotaoBorda
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro

@Composable
fun BotaoAlterar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .width(170.dp)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, OfertaBotaoBorda),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                contentColor = VerdeEscuro
            ),
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 0.dp
            )
        ) {
            Text(
                text = "Alterar senha",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = VerdeEscuro
            )
        }
    }
}