package com.example.ecoleta_estabelecimento.features.cadastro

import androidx.compose.foundation.lazy.LazyListScope
import com.example.ecoleta_estabelecimento.features.cadastro.components.CampoDeCadastro
import com.example.ecoleta_estabelecimento.features.cadastro.components.CardInicial

fun LazyListScope.CadastroScreen(
    viewModel: CadastroViewModel
){
    item {
        CardInicial()
    }


    item {
        CampoDeCadastro()
    }

}