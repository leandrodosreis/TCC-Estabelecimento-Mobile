package com.example.ecoleta_estabelecimento.features.cadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import com.example.ecoleta_estabelecimento.features.cadastro.components.CampoDeCadastro
import com.example.ecoleta_estabelecimento.features.cadastro.CadastroViewModel



@Composable
fun LazyListScope.CadastroScreen(
    viewModel: CadastroViewModel,
    onSair: ()-> Unit
){

    item { CampoDeCadastro( viewModel = viewModel, onSair = onSair) }

}

