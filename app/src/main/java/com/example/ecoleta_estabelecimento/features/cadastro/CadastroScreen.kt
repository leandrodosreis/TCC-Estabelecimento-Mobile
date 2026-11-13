package com.example.ecoleta_estabelecimento.features.cadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ecoleta_estabelecimento.features.cadastro.components.CampoDeCadastro
import com.example.ecoleta_estabelecimento.features.cadastro.components.CardInicial
@Composable
fun CardInicial(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {

    }
}

fun LazyListScope.CadastroScreen(
    viewModel: CadastroViewModel
){

    item {
        CampoDeCadastro()
    }

}