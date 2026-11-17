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
import com.example.ecoleta_estabelecimento.features.home.HomeScreen
import com.example.ecoleta_estabelecimento.features.home.HomeViewModel
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
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
                onNavigate("residuos")
            },

            // Botão para acessar Coletas
            onVerTodasOfertas = {
                onNavigate("coletas")
            },

            // Clique em uma oferta
            onVerOferta = { idOferta ->
                println("Oferta selecionada: $idOferta")
            }
        )
    }
}