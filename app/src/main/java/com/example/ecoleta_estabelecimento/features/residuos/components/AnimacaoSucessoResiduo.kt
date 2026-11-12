package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar

// Exibe a animação de sucesso após o cadastro de um resíduo
// A animação é carregada do arquivo: res/raw/animacao_sucesso.json

@Composable
fun AnimacaoSucessoResiduo(
    modifier: Modifier = Modifier,
    mensagem: String = "Resíduo cadastrado com sucesso!"
) {

    // Carrega a animação Lottie salva no projeto
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animacao_sucesso)
    )

    // Executa a animação uma única vez
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Check verde com os efeitos de celebração
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        // Mensagem exibida abaixo da animação
        Text(
            text = mensagem,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Branco
        )
    }
}