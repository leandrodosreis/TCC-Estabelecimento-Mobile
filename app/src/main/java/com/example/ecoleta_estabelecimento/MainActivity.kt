package com.example.ecoleta_estabelecimento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.home.HomeScreen
import com.example.ecoleta_estabelecimento.features.home.HomeViewModel
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.EcoletaEstabelecimentoTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecoleta_estabelecimento.features.cadastro.CadastroScreen
import com.example.ecoleta_estabelecimento.features.cadastro.CadastroViewModel
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosScreen
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.residuos.components.AnimacaoSucessoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogCancelarResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogDetalhesColeta
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogNovoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoletaEstabelecimentoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Branco
                ) { innerPadding ->
                    TelaInicial(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Composable responsável por controlar a tela exibida
@Composable
fun TelaInicial(
    modifier: Modifier = Modifier
) {

    // Controla temporariamente a rota selecionada
    var telaAtual by remember {
        mutableStateOf("home")
    }

    // Obtém a ViewModel do Cadastro
    val cadastroViewModel: CadastroViewModel = viewModel()

    // Obtém a ViewModel da Home
    val homeViewModel: HomeViewModel = viewModel()

    // Obtém a ViewModel de Meus Resíduos
    val residuosViewModel: ResiduosViewModel = viewModel()

    // Guarda o resíduo selecionado para cancelamento
    var residuoParaCancelar by remember {
        mutableStateOf<Residuo?>(null)
    }

    // Guarda o resíduo selecionado para edição
    var residuoParaEditar by remember {
        mutableStateOf<Residuo?>(null)
    }

    // Guarda o resíduo selecionado para visualizar os detalhes da coleta
    var residuoParaVerColeta by remember {
        mutableStateOf<Residuo?>(null)
    }

    // Controla a animação de sucesso da edição
    var mostrarAnimacaoEdicao by remember {
        mutableStateOf(false)
    }

    // Fecha automaticamente a animação após três segundos
    LaunchedEffect(mostrarAnimacaoEdicao) {
        if (mostrarAnimacaoEdicao) {
            delay(3000)
            mostrarAnimacaoEdicao = false
        }
    }

    // Fecha o popup quando a API confirmar o cancelamento
    LaunchedEffect(residuosViewModel.idResiduoCancelado) {
        val idCancelado = residuosViewModel.idResiduoCancelado

        if (idCancelado != null) {
            if (residuoParaCancelar?.idResiduo == idCancelado) {
                residuoParaCancelar = null
            }

            residuosViewModel.limparEstadoCancelamentoResiduo()
        }
    }

    // Atualiza automaticamente os dados da Home
    LaunchedEffect(telaAtual) {

        if (telaAtual == "home") {

            while (true) {
                // Aguarda 5 segundos
                delay(5_000)

                // Busca os dados atualizados da API
                homeViewModel.buscarHome()
            }
        }
    }

    // Dados recebidos da API
    val dadosHome = homeViewModel.dadosHome

    // Converte as ofertas da API para o modelo dos cards
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

    // Exibe o layout compartilhado das telas internas
    LayoutPadrao(
        modifier = modifier,
        rotaAtual = telaAtual,
        onNavigate = { rota ->
            telaAtual = rota
        },
        onLogout = {
            telaAtual = "cadastro"
        }
    ) {

        // Exibe a tela correspondente à rota selecionada
        when (telaAtual) {

            "home" -> {
                // Conteúdo da Home
                HomeScreen(
                    resumo = dadosHome?.resumo,
                    ofertas = ofertas,

                    onCadastrarResiduo = {
                        telaAtual = "residuos"
                    },
                    onVerTodasOfertas = {
                        telaAtual = "coletas"
                    },
                    onVerOferta = { idOferta ->
                        println("Oferta selecionada: $idOferta")
                    }
                )

                // Estado de carregamento
                if (homeViewModel.carregando) {
                    item {
                        Text("Carregando dados da Home...")
                    }
                }

                // Estado de erro
                homeViewModel.mensagemErro?.let { erro ->
                    item {
                        Text(erro)
                    }
                }
            }

            // Telas temporárias para testar o menu
            "residuos" -> {
                ResiduosScreen(
                    viewModel = residuosViewModel,
                    onCancelarResiduo = { residuo ->
                        residuosViewModel.limparEstadoCancelamentoResiduo()
                        residuoParaCancelar = residuo
                    },
                    onEditarResiduo = { residuo ->
                        residuoParaEditar = residuo
                    },
                    onVerColeta = { residuo ->
                        // Guarda qual resíduo foi selecionado
                        residuoParaVerColeta = residuo

                        // Busca os detalhes reais da coleta na API
                        residuosViewModel.buscarDetalhesColeta(residuo.idResiduo)
                    }
                )
            }

            "coletas" -> item {
                Text("Tela de Coletas")
            }

            "historico" -> item {
                Text("Tela de Histórico")
            }

            "perfil" -> item {
                Text("Tela de Perfil")
            }

            "cadastro" -> {
                CadastroScreen(viewModel = cadastroViewModel)
            }

            "login" -> item {
                Text("Tela de Login (teste)")
            }
        }
    }

    // Exibe o popup de cancelamento sobre a tela
    residuoParaCancelar?.let { residuo ->
        DialogCancelarResiduo(
            residuo = residuo,
            viewModel = residuosViewModel,

            onDismiss = {
                if (!residuosViewModel.cancelandoResiduo) {
                    residuosViewModel.limparEstadoCancelamentoResiduo()
                    residuoParaCancelar = null
                }
            },

            onConfirmar = { idResiduo ->
                residuosViewModel.cancelarResiduo(idResiduo)
            }
        )
    }

    // Exibe o popup de edição sobre a tela
    residuoParaEditar?.let { residuo ->
        DialogNovoResiduo(
            residuoParaEditar = residuo,
            viewModel = residuosViewModel,

            onDismiss = {
                residuoParaEditar = null
            },

            // O cadastro continua sendo controlado pelo ResiduosScreen
            onCadastroSucesso = {},

            // Será utilizado quando a API confirmar a edição
            onEdicaoSucesso = {
                residuoParaEditar = null
                mostrarAnimacaoEdicao = true
            }
        )
    }

    // Exibe a mesma animação Lottie após a edição
    if (mostrarAnimacaoEdicao) {
        Dialog(
            onDismissRequest = {
                if (!residuosViewModel.editandoResiduo) {
                    residuoParaEditar = null
                }
            }
        ) {
            AnimacaoSucessoResiduo(
                mensagem = "Resíduo editado com sucesso!"
            )
        }
    }

    // Exibe o popup com os detalhes da coleta selecionada
    if (residuoParaVerColeta != null) {
        DialogDetalhesColeta(
            viewModel = residuosViewModel,
            onDismiss = {
                // Fecha o popup
                residuoParaVerColeta = null

                // Limpa os detalhes da consulta anterior
                residuosViewModel.limparDetalhesColeta()
            }
        )
    }
}