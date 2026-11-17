package com.example.ecoleta_estabelecimento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecoleta_estabelecimento.components.header.HeaderScreen
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.components.navigation.BarraNavegacaoInferior
import com.example.ecoleta_estabelecimento.features.coletas.ColetasViewModel
import com.example.ecoleta_estabelecimento.features.home.HomeViewModel
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.routes.ColetasRoute
import com.example.ecoleta_estabelecimento.features.routes.HomeRoute
import com.example.ecoleta_estabelecimento.features.routes.ResiduosRoute
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.EcoletaEstabelecimentoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoletaEstabelecimentoTheme {

                // Controlador responsável pelas rotas do aplicativo
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Branco
                ) { innerPadding ->
                    // Gerencia todas as telas
                    NavegacaoEcoleta(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Rotas disponíveis no aplicativo
private object Rotas {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"

    const val HOME = "home"
    const val RESIDUOS = "residuos"
    const val COLETAS = "coletas"
    const val HISTORICO = "historico"
    const val PERFIL = "perfil"
}

// Define a direção da animação com base na posição das telas na barra inferior
private fun direcaoEntreTelas(
    origem: String?,
    destino: String?
): AnimatedContentTransitionScope.SlideDirection {
    // Mesma ordem dos botões da BarraNavegacaoInferior
    val ordemTelas = listOf(
        Rotas.HOME,
        Rotas.COLETAS,
        Rotas.RESIDUOS,
        Rotas.HISTORICO,
        Rotas.PERFIL
    )

    val indiceOrigem = ordemTelas.indexOf(origem)
    val indiceDestino = ordemTelas.indexOf(destino)

    // Login e Cadastro não fazem parte da barra inferior
    if (indiceOrigem == -1 || indiceDestino == -1) {
        return AnimatedContentTransitionScope.SlideDirection.Left
    }

    // Indo para uma tela à direita
    return if (indiceDestino > indiceOrigem) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        // Indo para uma tela à esquerda
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}

// Controla as telas
@Composable
private fun NavegacaoEcoleta(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // ===============================
    // ViewModels utilizadas nas telas
    // ===============================

    val homeViewModel: HomeViewModel = viewModel()
    val residuosViewModel: ResiduosViewModel = viewModel()
    val coletasViewModel: ColetasViewModel = viewModel()

    // Função utilizada ao clicar em Sair
    val onLogout: () -> Unit = {

        // Limpa os estados auxiliares dos diálogos
        residuosViewModel.limparEstadoCancelamentoResiduo()
        residuosViewModel.limparDetalhesColeta()

        coletasViewModel.limparDetalhesOferta()
        coletasViewModel.limparEstadoAceitarOferta()
        coletasViewModel.limparEstadoRecusarOferta()

        // Envia o usuário para Login
        navController.sairParaLogin()
    }

    // NAVHOST - NAVEGAÇÃO PRINCIPAL

    // IDENTIFICA QUAL TELA ESTÁ ABERTA
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val rotaAtual = navBackStackEntry?.destination?.route

    // Header e barra inferior aparecem somente nas telas internas
    val mostrarNavegacao = rotaAtual in listOf(
        Rotas.HOME,
        Rotas.COLETAS,
        Rotas.RESIDUOS,
        Rotas.HISTORICO,
        Rotas.PERFIL
    )

    // ESTRUTURA PRINCIPAL DO APLICATIVO

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // HEADER FIXO
        if (mostrarNavegacao) {
            HeaderScreen(
                rotaAtual = rotaAtual ?: Rotas.HOME,

                // Clique na logo para voltar à Home
                onNavigate = { rota ->
                    navController.navegarMenu(rota)
                },

                // Botão Sair
                onLogout = onLogout
            )
        }

        // NAVHOST - SOMENTE CONTEÚDO CENTRAL

        NavHost(
            navController = navController,
            // Deve começar no login, quando estiver pronto
            startDestination = Rotas.HOME,
            modifier = Modifier.weight(1f),

            // Entrada da nova tela
            enterTransition = {
                val direcao = direcaoEntreTelas(
                    origem = initialState.destination.route,
                    destino = targetState.destination.route
                )

                slideIntoContainer(
                    towards = direcao,
                    animationSpec = tween(250)
                )
            },

            // Saída da tela atual
            exitTransition = {
                val direcao = direcaoEntreTelas(
                    origem = initialState.destination.route,
                    destino = targetState.destination.route
                )

                slideOutOfContainer(
                    towards = direcao,
                    animationSpec = tween(250)
                )
            },

            // Entrada ao voltar
            popEnterTransition = {
                val direcao = direcaoEntreTelas(
                    origem = initialState.destination.route,
                    destino = targetState.destination.route
                )

                slideIntoContainer(
                    towards = direcao,
                    animationSpec = tween(250)
                )
            },

            // Saída ao voltar
            popExitTransition = {
                val direcao = direcaoEntreTelas(
                    origem = initialState.destination.route,
                    destino = targetState.destination.route
                )

                slideOutOfContainer(
                    towards = direcao,
                    animationSpec = tween(250)
                )
            }
        ) {
            // LOGIN - TEMPORÁRIO
            composable(route = Rotas.LOGIN) {
                LoginTemporario(
                    onEntrarTeste = {
                        // Acesso temporário para testar a navegação
                        navController.navigate(Rotas.HOME) {
                            popUpTo(Rotas.LOGIN) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    },

                    onCadastro = {
                        navController.navigate(Rotas.CADASTRO)
                    }
                )
            }

            // CADASTRO - TEMPORÁRIO
            composable(route = Rotas.CADASTRO) {
                CadastroTemporario(
                    onVoltarLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // =====
            // HOME
            // =====

            composable(route = Rotas.HOME) {
                HomeRoute(
                    viewModel = homeViewModel,
                    onNavigate = { rota ->
                        navController.navegarMenu(rota)
                    },
                    onLogout = onLogout
                )
            }

            // ==============
            // MEUS RESIDUOS
            // ==============

            composable(route = Rotas.RESIDUOS) {
                ResiduosRoute(
                    navController = navController,
                    viewModel = residuosViewModel,
                    onNavigate = { rota ->
                        navController.navegarMenu(rota)
                    },
                    onLogout = onLogout
                )
            }

            // ========
            // COLETAS
            // ========

            composable(route = Rotas.COLETAS) {
                ColetasRoute(
                    navController = navController,
                    viewModel = coletasViewModel,
                    onNavigate = { rota ->
                        navController.navegarMenu(rota)
                    },
                    onLogout = onLogout
                )
            }

            // ==========
            // HISTÓRICO
            // ==========

            composable(route = Rotas.HISTORICO) {
                TelaInterna(
                    rotaAtual = Rotas.HISTORICO,
                    navController = navController,
                    onLogout = onLogout
                ) {
                    item {
                        Text("Tela de Histórico")
                    }
                }
            }

            // ========
            // PERFIL
            // ========

            composable(route = Rotas.PERFIL) {
                TelaInterna(
                    rotaAtual = Rotas.PERFIL,
                    navController = navController,
                    onLogout = onLogout
                ) {
                    item {
                        Text("Tela de Perfil")
                    }
                }
            }
        }

        // BARRA INFERIOR FIXA
        if (mostrarNavegacao) {
            BarraNavegacaoInferior(
                rotaAtual = rotaAtual ?: Rotas.HOME,

                onNavigate = { rota ->
                    navController.navegarMenu(rota)
                }
            )
        }
    }
}

// ================================
// Conteúdo interno entre as telas
// ================================
@Composable
private fun TelaInterna(
    rotaAtual: String,
    navController: NavHostController,
    onLogout: () -> Unit,
    conteudo: LazyListScope.() -> Unit
) {
    LayoutPadrao(
        modifier = Modifier.fillMaxSize(),

        // Informa qual tela deve ficar destacada na BarraNavegacaoInferior
        rotaAtual = rotaAtual,
        // Utilizado tanto pela logo do Header quanto pelos cinco botões inferiores
        onNavigate = { rota ->
            navController.navegarMenu(rota)
        },

        // Utilizado pelo botão Sair do Header
        onLogout = onLogout
    ) {
        conteudo()
    }
}

private fun NavHostController.navegarMenu(
    rota: String
) {
    // Evita navegar novamente para a tela atual
    if (currentDestination?.route == rota) {
        return
    }

    navigate(rota) {
        // Evita acumular várias cópias das telas
        popUpTo(graph.findStartDestination().id)

        launchSingleTop = true
    }
}


// ============================
// Sair do aplicativo
// ============================

private fun NavHostController.sairParaLogin() {
    navigate(Rotas.LOGIN) {
        // Remove todas as telas internas do histórico, incluindo o destino inicial
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }

        // Evita criar cópias desnecessárias do Login
        launchSingleTop = true
    }
}

// =================
// Login temporário
// =================

@Composable
fun LoginTemporario(
    onEntrarTeste: () -> Unit,
    onCadastro: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Tela de Login (em construção)")

        Button(
            onClick = onEntrarTeste
        ) {
            Text("Ir para Home (teste)")
        }

        TextButton(
            onClick = onCadastro
        ) {
            Text("Não possui conta? Cadastre-se")
        }
    }
}

// ====================
// Cadastro temporário
// ====================

@Composable
private fun CadastroTemporario(
    onVoltarLogin: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Tela de Cadastro (em construção)")

        TextButton(
            onClick = onVoltarLogin
        ) {
            Text("Voltar para Login")
        }
    }
}