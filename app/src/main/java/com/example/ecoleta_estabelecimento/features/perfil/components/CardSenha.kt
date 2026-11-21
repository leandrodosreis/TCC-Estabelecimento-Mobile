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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

@Composable
fun CardSenha(
    modifier: Modifier = Modifier,
    onAlterarSenhaClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título da seção
            Text(
                text = "SEGURANÇA",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textoPainel
            )
            // Indicação visual da senha
            // Os pontos não representam a senha real
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text = "Senha",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = subTitulo
                )

                Text(
                    text = "••••••••",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = subTitulo
                )
            }

            // Abre o popup por meio do callback
            BotaoAlterar(
                onClick = onAlterarSenhaClick
            )
        }
    }
}