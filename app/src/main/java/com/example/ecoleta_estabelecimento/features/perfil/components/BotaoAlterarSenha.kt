package com.example.ecoleta_estabelecimento.features.perfil.components

import android.R.attr.onClick
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun BotaoAlterar(
    onClick: () -> Unit
) {


    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp),
        shape = RoundedCornerShape(17.dp),
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

