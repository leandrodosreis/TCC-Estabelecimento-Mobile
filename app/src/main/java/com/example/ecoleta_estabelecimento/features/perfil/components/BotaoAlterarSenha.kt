package com.example.ecoleta_estabelecimento.features.perfil.components

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.Preto
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun BotaoAlterar(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(170.dp)
                .height(35.dp),
            shape = RoundedCornerShape(9.dp),
            border = BorderStroke(1.dp, Preto),
            colors = ButtonDefaults.buttonColors(
                containerColor = Branco,
                contentColor = Branco
            )

        ) {
            Text(
                text = "Alterar senha",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

    }


}

