package com.example.ecoleta_estabelecimento.features.coletas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.features.residuos.components.AnimacaoSucessoResiduo
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import kotlinx.coroutines.delay

@Composable
fun DialogOfertaAceitaSucesso(
    onConcluir: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        LaunchedEffect(Unit) {
            delay(2200)
            onConcluir()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeEscuro),
            contentAlignment = Alignment.Center
        ) {

            AnimacaoSucessoResiduo(
                mensagem = "A oferta foi aceita!"
            )
        }
    }
}