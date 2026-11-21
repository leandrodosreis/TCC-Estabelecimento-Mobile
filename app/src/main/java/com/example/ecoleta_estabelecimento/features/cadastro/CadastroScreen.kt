package com.example.ecoleta_estabelecimento.features.cadastro

import androidx.compose.foundation.lazy.LazyListScope
import com.example.ecoleta_estabelecimento.features.cadastro.components.CampoDeCadastro

fun LazyListScope.CadastroScreen(
    viewModel: CadastroViewModel,
    onSair: () -> Unit
) {
    item {
        CampoDeCadastro(
            viewModel = viewModel,
            onSair = onSair
        )
    }
}