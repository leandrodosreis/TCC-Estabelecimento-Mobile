package com.example.ecoleta_estabelecimento.features.routes

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecoleta_estabelecimento.features.cadastro.CadastroViewModel
import com.example.ecoleta_estabelecimento.features.cadastro.components.CampoDeCadastro

@Composable
fun CadastroRoute(
    onVoltarLogin: () -> Unit
) {
    // Recupera a ViewModel vinculada à rota de Cadastro
    val viewModel: CadastroViewModel = viewModel()

    // Context utilizado para apresentar mensagens temporárias
    val context = LocalContext.current

    // Observa quando o backend confirma a realização do cadastro
    LaunchedEffect(viewModel.cadastroRealizado) {
        if (viewModel.cadastroRealizado) {

            // Informa que o cadastro foi realizado
            Toast.makeText(
                context,
                "Cadastro realizado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            // Retorna para a tela de Login
            onVoltarLogin()
        }
    }

    // Monta a interface utilizando os estados da ViewModel
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            CampoDeCadastro(
                viewModel = viewModel,
                onSair = {
                    onVoltarLogin()
                }
            )
        }
    }
}