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
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.home.HomeScreen
import com.example.ecoleta_estabelecimento.features.home.HomeViewModel
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.EcoletaEstabelecimentoTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoletaEstabelecimentoTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),containerColor = Branco) { innerPadding ->
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

    // Obtém a ViewModel da Home
    val homeViewModel: HomeViewModel = viewModel()

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
        OfertaResumo (
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
            telaAtual = "login"
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
            "residuos" -> item {
                Text("Tela de Meus Resíduos")
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

            "login" -> item {
                Text("Tela de Login (teste)")
            }
        }
    }
}