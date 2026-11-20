package com.example.ecoleta_estabelecimento.features.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.ecoleta_estabelecimento.components.DialogSucessoEcoleta.DialogSucessoEcoleta
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.home.HomeScreen
import com.example.ecoleta_estabelecimento.features.home.HomeViewModel
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogNovoResiduo
import kotlinx.coroutines.delay

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    residuosViewModel: ResiduosViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    // Busca os dados ao entrar na Home
    LaunchedEffect(Unit) {
        viewModel.buscarHome()
    }

    var atualizacaoManual by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.carregando) {
        if (!viewModel.carregando) {
            atualizacaoManual = false
        }
    }

    // Dados recebidos da API
    val dadosHome = viewModel.dadosHome

    // Controla a abertura do popup de cadastro pela Home
    var mostrarDialogNovoResiduo by remember {
        mutableStateOf(false)
    }

    // Controla a animação de sucesso após o cadastro
    var mostrarAnimacaoSucesso by remember {
        mutableStateOf(false)
    }

    // Fecha automaticamente a animação de sucesso
    LaunchedEffect(mostrarAnimacaoSucesso) {
        if (mostrarAnimacaoSucesso) {
            delay(2000)
            mostrarAnimacaoSucesso = false
            onNavigate("residuos")
        }
    }

    // Converte as ofertas para o formato da HomeScreen
    val ofertas = dadosHome?.ofertas?.map { oferta ->
        OfertaResumo(
            id = oferta.idOfertaInicial,
            nomeColetor = oferta.nomeEmpresaColetora,
            material = oferta.material,
            quantidadeKg = oferta.quantidade,
            data = oferta.dataDisponivel,
            valor = oferta.valorOfertado
                .replace(",", ".")
                .toDoubleOrNull() ?: 0.0,
            status = oferta.statusOferta
        )
    } ?: emptyList()

    LayoutPadrao(
        modifier = Modifier.fillMaxSize(),
        rotaAtual = "home",
        onNavigate = onNavigate,
        onLogout = onLogout,

        // Mostra o indicador enquanto a API carrega
        atualizando = atualizacaoManual && viewModel.carregando,

        // Busca novamente os dados ao puxar para baixo
        onAtualizar = {
            if (!viewModel.carregando) {
                atualizacaoManual = true
                viewModel.buscarHome()
            }
        }
    ) {
        HomeScreen(
            resumo = dadosHome?.resumo,
            ofertas = ofertas,
            carregando = viewModel.carregando,
            erro = viewModel.mensagemErro,

            // Botão para acessar Meus Resíduos
            onCadastrarResiduo = {
                mostrarDialogNovoResiduo = true
            },

            // Botão para acessar Coletas
            onVerTodasOfertas = {
                onNavigate("coletas")
            },

            // Clique em uma oferta
            onVerOferta = { idOferta ->
                onNavigate("coletas/oferta/$idOferta")
            }
        )
    }


    // ==========================================
    // POPUP DE CADASTRO DE RESÍDUO PELA HOME
    // ==========================================
    if (mostrarDialogNovoResiduo) {
        // Reutiliza o mesmo popup de Meus Resíduos
        DialogNovoResiduo(
            viewModel = residuosViewModel,

            // Fecha quando o usuário cancelar
            onDismiss = {
                mostrarDialogNovoResiduo = false
            },

            // Executado quando o cadastro for confirmado
            // com sucesso pela API
            onCadastroSucesso = {
                // Fecha o formulário
                mostrarDialogNovoResiduo = false
                // Atualiza os dados apresentados na Home
                viewModel.buscarHome()
                // Exibe a mesma animação de sucesso
                mostrarAnimacaoSucesso = true
            }
        )
    }

    // ==========================================
    // ANIMAÇÃO DE SUCESSO DO CADASTRO
    // ==========================================

    if (mostrarAnimacaoSucesso) {

        Dialog(
            onDismissRequest = {}
        ) {
            // Reutiliza a animação já usada na tela de Meus Resíduos
            DialogSucessoEcoleta(
                mensagem = "Resíduo cadastrado com sucesso!",
                onConcluir = {
                    mostrarAnimacaoSucesso = false
                }
            )
        }
    }
}