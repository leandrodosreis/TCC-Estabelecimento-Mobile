package com.example.ecoleta_estabelecimento

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecoleta_estabelecimento.auth.SessionManager
import com.example.ecoleta_estabelecimento.auth.TokenSessao
import com.example.ecoleta_estabelecimento.components.header.HeaderScreen
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.components.navigation.BarraNavegacaoInferior
import com.example.ecoleta_estabelecimento.features.coletas.ColetasViewModel
import com.example.ecoleta_estabelecimento.features.home.HomeViewModel
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.routes.ColetasRoute
import com.example.ecoleta_estabelecimento.features.routes.HomeRoute
import com.example.ecoleta_estabelecimento.features.routes.LoginRoute
import com.example.ecoleta_estabelecimento.features.routes.ResiduosRoute
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.EcoletaEstabelecimentoTheme
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoletaEstabelecimentoTheme {
                // Antes de montar a navegação, verifica se existe uma sessão válida
                EntradaEcoleta()
            }
        }
    }
}

// Controla a inicialização do aplicativo
// Antes de criar as telas e as ViewModels, verifica se existe um JWT salvo e ainda válido
// Se existir, inicia na Home, caso contrário, inicia no Login
@Composable
private fun EntradaEcoleta() {
    // Obtém o contexto do aplicativo
    val context = LocalContext.current

    // Guarda qual será a primeira rota,
    // null significa que a sessão ainda está sendo verificada
    var destinoInicial by remember {
        mutableStateOf<String?>(null)
    }

    // Executado quando essa função entra na composição
    LaunchedEffect(Unit) {
        // Recupera o token fora da thread principal,
        // pois o SessionManager utiliza criptografia e armazenamento local
        val possuiSessaoValida = withContext(
            Dispatchers.IO
        ) {
            val sessionManager = SessionManager(context)

            // recuperarToken:
            // - Recupera o JWT criptografado
            // - Verifica a expiração
            // - Coloca o JWT no TokenSessão
            // - Retorna null se não houver sessão utilizável
            sessionManager.recuperarToken() != null
        }

        // Define a tela inicial somente depois de terminar a verificação da sessão
        destinoInicial = if (possuiSessaoValida) {
            Rotas.HOME
        } else {
            Rotas.LOGIN
        }
    }

    // Enquanto a tela inicial não é confirmada, é exibido apenas o carregamento
    if (destinoInicial == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFundo),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = VerdePrincipal
            )
        }
    } else {
        // Só cria o NavController depois de verificar a sessão
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Branco
        ) { innerPadding ->
            // A navegação recebe a rota inicial escolhida pela verificação do JWT
            NavegacaoEcoleta(
                navController = navController,
                destinoInicial = destinoInicial!!,
                modifier = Modifier.padding(innerPadding)
            )
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
    destinoInicial: String,
    modifier: Modifier = Modifier
) {
    // ===============================
    // ViewModels utilizadas nas telas
    // ===============================

    // Context necessário para acessar o armazenamento do app
    val context = LocalContext.current

    // Escopo utilizado para executar o logout sem bloquear a interface do usuário
    val scope = rememberCoroutineScope()

    // Reutiliza a instância do SessionManager durante as recomposições desta função
    val sessionManager = remember(context) {
        SessionManager(context.applicationContext)
    }

    val homeViewModel: HomeViewModel = viewModel()
    val residuosViewModel: ResiduosViewModel = viewModel()
    val coletasViewModel: ColetasViewModel = viewModel()

    // Função utilizada ao clicar em Sair
    val onLogout: () -> Unit = {

        // Executa o encerramento em uma coroutine
        scope.launch {
            // A remoção do JWT utiliza armazenamento local, por isso é executada fora da thread principal
            val sessaoRemovida = try {
                withContext(Dispatchers.IO) {
                    sessionManager.limparSessao()
                }
            } catch (e: Exception) {
                // Se ocorrer uma falha inesperada, não confirmamos o logout
                false
            }

            if (sessaoRemovida) {
                // Limpa os estados auxiliares dos resíduos
                residuosViewModel.limparEstadoCancelamentoResiduo()
                residuosViewModel.limparDetalhesColeta()

                // Limpa os estados auxiliares das coletas
                coletasViewModel.limparDetalhesOferta()
                coletasViewModel.limparEstadoAceitarOferta()
                coletasViewModel.limparEstadoRecusarOferta()

                // Após limpar o JWT, navega para o Login
                navController.sairParaLogin()
            } else {
                // Se a remoção falhar, é informado ao usuário
                Toast.makeText(
                    context,
                    "Não foi possível encerrar a sessão. Tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // NAVHOST - NAVEGAÇÃO PRINCIPAL

    // IDENTIFICA QUAL TELA ESTÁ ABERTA
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val rotaAtual = navBackStackEntry?.destination?.route

    // =====================================
    // Monitoramento da expiração da sessão
    // =====================================

    // Obtém o ciclo de vida da Activity
    // Será utilizado para detectar quando o app voltar ao primeiro plano (quando não estiver aberto em segundo plano)
    val lifecycleOwner = LocalLifecycleOwner.current

    // Contador atualizado sempre que a Activity recebe o evento ON_RESUME
    // Quando ele mudar, o LaunchedEffect abaixo executará uma nova verificação do JWT
    var verificacoesRetorno by remember {
        mutableStateOf(0)
    }

    // Observa o ciclo de vida enquanto esta função estiver presente na composição
    DisposableEffect(lifecycleOwner) {
        // Cria um observador dos eventos da Activity
        val observador = LifecycleEventObserver { _, evento ->
            // ON_RESUME acontece quando a Activity retorna ao estado de interação
            if (evento == Lifecycle.Event.ON_RESUME) {
                verificacoesRetorno++
            }
        }

        // Registra o observador no ciclo de vida
        lifecycleOwner.lifecycle.addObserver(observador)

        // Remove o observador quando a composição deixar de utilizá-lo
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observador)
        }
    }

    // Executa o monitoramento quando:
    // - A rota atual muda
    // - O aplicativo retorna ao primeiro plano
    // LaunchedEffect cancela a execução anterior quando uma dessas chaves muda
    LaunchedEffect(rotaAtual, verificacoesRetorno) {
        // Verifica somente nas telas autenticadas
        // Login e Cadastro não exigem sessão

        val telaAutenticada = rotaAtual in listOf(
            Rotas.HOME,
            Rotas.RESIDUOS,
            Rotas.COLETAS,
            Rotas.HISTORICO,
            Rotas.PERFIL
        )

        if (!telaAutenticada) {
            return@LaunchedEffect
        }

        // Obtém o JWT em memória
        val token = TokenSessao.getToken()

        // Se uma tela autenticada estiver aberta sem JWT, encerra a sessão para não deixar a tela desprotegida
        if (token == null) {
            onLogout()
            return@LaunchedEffect
        }

        // Cria um SessionManager para consultar a expiração informada pelo backend
        val sessionManager = SessionManager(
            context.applicationContext
        )

        // Obtém o campo "exp", em segundos Unix
        val expiracao = sessionManager.obterExpiracao(token)

        // Obtém o instante atual, também em segundos Unix
        val agora = System.currentTimeMillis() / 1000L

        // Se o token estiver malformado ou expirado, encerra a sessão imediatamente
        if (expiracao == null || expiracao <= agora) {
            onLogout()
            return@LaunchedEffect
        }

        // Converte a expiração para milissegundos e calcula o tempo restante da sessão
        val tempoRestante = (expiracao * 1000L) - System.currentTimeMillis()

        // Suspende a coroutine até o instante esperado de expiração do JWT
        // delay não bloqueia a interface
        if (tempoRestante > 0L) {
            delay(tempoRestante)
        }

        // Antes de encerrar, confirma que o monitoramento continue com o mesmo JWT
        // Também verifica novamente a expiração
        // Isso evita encerrar uma sessão nova por causa de um temporizador antigo
        if (TokenSessao.getToken() == token && !sessionManager.tokenEstaValido(token)) {
            onLogout()
        }
    }


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
            // Recebe a rota definida pela EntradaEcoleta:
            // HOME para sessão válida ou LOGIN para sessão ausente/expirada
            startDestination = destinoInicial,
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
            // ======
            // LOGIN
            // ======
            composable(route = Rotas.LOGIN) {
                LoginRoute(
                    navController = navController,
                    // Navega para a tela separada de cadastro
                    onCadastro = {
                        navController.navigate(Rotas.CADASTRO)
                    },
                    // Executado somente depois que o backend autenticar e o JWT for salvo com sucesso
                    onLoginSucesso = {
                        navController.navigate(Rotas.HOME) {
                            // Remove o Login do histórico de navegação
                            popUpTo(Rotas.LOGIN) {
                                inclusive = true
                            }
                            // Evita criar uma Home duplicada
                            launchSingleTop = true
                        }
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
        // Mantém a Home como base da navegação interna
        popUpTo(Rotas.HOME)

        launchSingleTop = true
    }
}


// ============================
// Sair do aplicativo
// ============================

private fun NavHostController.sairParaLogin() {
    navigate(Rotas.LOGIN) {
        // Remove todas as telas anteriores e mantém apenas o grafo de navegação
        popUpTo(graph.id) {
            inclusive = false
        }

        // Evita criar cópias desnecessárias do Login
        launchSingleTop = true
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