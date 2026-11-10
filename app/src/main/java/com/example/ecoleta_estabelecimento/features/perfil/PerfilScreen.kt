package com.example.ecoleta_estabelecimento.features.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.home.components.CardBoasVindas
import com.example.ecoleta_estabelecimento.features.perfil.componenents.CardResumos
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel


fun LazyListScope.PerfilScreen(
    viewModel: PefilViewModel
) {

    item {
        Column {
            Text(
                text = "PAINEL DO ESTABELECIMENTO",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = textoPainel
            )

            // Espaçamento entre título e Nome da Tela
            Spacer(modifier = Modifier.height(10.dp))

            // Titulo da tela
            Text(
                text = "Meu perfil",
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                color = nomeTela
            )

            // Espaçamento entre Nome da Tela e subTitulo
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitulo
            Text(
                text = "Gerencie e acompanhe suas operações na Ecoleta.",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = subTitulo
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // Card grande de boas-vindas
    item {
        CardResumos(
            nomeEmpresa = "Teste",
            cnpjEmpresa = "40028922"
        )
    }

}