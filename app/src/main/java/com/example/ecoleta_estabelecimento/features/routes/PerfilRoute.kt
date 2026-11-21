package com.example.ecoleta_estabelecimento.features.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.perfil.PerfilScreen
import com.example.ecoleta_estabelecimento.features.perfil.PerfilViewModel
import com.example.ecoleta_estabelecimento.features.perfil.components.DialogAlterarSenha
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha

@Composable
fun PerfilRoute(
    viewModel: PerfilViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    // Carregamento inicial
    // Busca os dados quando entrar no Perfil
    LaunchedEffect(Unit) {
        viewModel.carregarPerfil()
    }

    // Atualização manual
    // Controla o indicador ao puxar para atualizar
    var atualizacaoManual by remember {
        mutableStateOf(false)
    }

    // Encerra a animação quando a requisição terminar
    LaunchedEffect(viewModel.carregandoPerfil) {
        if (!viewModel.carregandoPerfil) {
            atualizacaoManual = false
        }
    }

    // Alteração de senha
    // Controla a abertura do popup de senha
    var mostrarDialogSenha by remember {
        mutableStateOf(false)
    }

    // Fecha o popup somente após o PATCH confirmar sucesso
    LaunchedEffect(viewModel.senhaAlterada) {
        if (viewModel.senhaAlterada) {
            mostrarDialogSenha = false
            viewModel.limparEstadoAlteracaoSenha()
        }
    }

    // Layout da tela
    LayoutPadrao(
        modifier = Modifier.fillMaxSize(),
        rotaAtual = "perfil",
        onNavigate = onNavigate,
        onLogout = onLogout,
        // Mostra o indicador apenas na atualização manual
        atualizando = atualizacaoManual && viewModel.carregandoPerfil,
        // Executa um novo GET ao puxar para baixo
        onAtualizar = {
            if (!viewModel.carregandoPerfil) {
                atualizacaoManual = true
                viewModel.carregarPerfil()
            }
        }
    ) {
        PerfilScreen(
            viewModel = viewModel,
            // Abre o popup e limpa os erros anteriores
            onAlterarSenhaClick = {
                viewModel.limparEstadoAlteracaoSenha()
                mostrarDialogSenha = true
            }
        )
    }

    // Popup de senha
    // Mantém o popup fora da lista rolável
    if (mostrarDialogSenha) {

        DialogAlterarSenha(
            onDismiss = {
                // Impede fechar durante o PATCH
                if (!viewModel.alterandoSenha) {
                    mostrarDialogSenha = false
                    viewModel.limparEstadoAlteracaoSenha()
                }
            },
            // Envia os dados para a ViewModel
            onSalvar = { senhaAtual, novaSenha, confirmarSenha ->
                viewModel.alterarSenha(
                    PerfilSenha(
                        senhaAtual = senhaAtual,
                        novaSenha = novaSenha,
                        confirmarSenha = confirmarSenha
                    )
                )
            },
            carregando = viewModel.alterandoSenha,
            mensagemErroApi = viewModel.erroAlteracaoSenha
        )
    }
}