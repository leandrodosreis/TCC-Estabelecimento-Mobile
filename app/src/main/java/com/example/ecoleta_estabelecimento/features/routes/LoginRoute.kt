package com.example.ecoleta_estabelecimento.features.routes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ecoleta_estabelecimento.features.login.LoginScreen
import com.example.ecoleta_estabelecimento.features.login.LoginViewModel

@Composable
fun LoginRoute(
    navController: NavHostController,
    onCadastro: () -> Unit,
    onLoginSucesso: () -> Unit
) {
    // Recupera a ViewModel vinculada à rota de Login
    val viewModel: LoginViewModel = viewModel()

    // Context utilizado para apresentar a mensagem temporária de recuperação de senha
    val context = LocalContext.current

    // Observa quando a API confirma a autenticação e o SessionManager consegue salvar o JWT
    LaunchedEffect(viewModel.loginRealizado) {
        if (viewModel.loginRealizado) {
            // A navegação fica sob responsabilidade da MainActivity, por meio do callback
            onLoginSucesso()
        }
    }

    // Monta a interface utilizando os estados da ViewModel
    LoginScreen(
        // Envia os dados para a API
        // Não navega diretamente para a Home
        onEntrar = { cnpj, senha ->
            viewModel.realizarLogin(
                cnpj = cnpj,
                senha = senha
            )
        },
        // Abre a tela separada de cadastro
        onCadastro = onCadastro,

        // Mantém a mensagem de recuperação de senha
        onEsqueceuSenha = {
            Toast.makeText(
                context,
                "Recuperação de senha em desenvolvimento",
                Toast.LENGTH_SHORT
            ).show()
        },

        // Desabilita os campos durante a requisição e mostra o indicador de carregamento
        carregando = viewModel.carregandoLogin,

        // Mensagem de erro devolvida pela ViewModel
        erroAutenticacao = viewModel.erroLogin,

        // Limpa o erro quando CNPJ ou senha forem alterados
        onLimparErroAutenticacao = {
            viewModel.limparEstadoLogin()
        }
    )
}