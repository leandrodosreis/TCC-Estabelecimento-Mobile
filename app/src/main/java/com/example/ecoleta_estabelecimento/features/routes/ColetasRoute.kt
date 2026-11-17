package com.example.ecoleta_estabelecimento.features.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.ecoleta_estabelecimento.components.DialogSucessoEcoleta.DialogSucessoEcoleta
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.coletas.ColetasScreen
import com.example.ecoleta_estabelecimento.features.coletas.ColetasViewModel
import com.example.ecoleta_estabelecimento.features.coletas.components.DialogConfirmarAceiteOferta
import com.example.ecoleta_estabelecimento.features.coletas.components.DialogDetalhesOferta
import com.example.ecoleta_estabelecimento.features.coletas.model.Coleta
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun ColetasRoute(
    navController: NavHostController,
    viewModel: ColetasViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {

    // Atualiza as ofertas sempre que entrar na tela de Coletas
    LaunchedEffect(Unit) {
        viewModel.carregarColetas()
    }

    var atualizacaoManual by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.carregandoColetas) {
        if (!viewModel.carregandoColetas) {
            atualizacaoManual = false
        }
    }

    // Oferta cujos detalhes estão abertos
    var idOfertaDetalhesAberto by remember {
        mutableStateOf<Int?>(null)
    }

    // Oferta aguardando confirmação
    var ofertaAConfirmar by remember {
        mutableStateOf<Coleta?>(null)
    }

    // Oferta aguardando confirmação de recusa
    var ofertaARecusar by remember {
        mutableStateOf<Coleta?>(null)
    }

    // Nome informado pelo funcionário
    var nomeFuncionarioAConfirmar by remember {
        mutableStateOf("")
    }

    // Fecha os detalhes após recusa confirmada pela API
    LaunchedEffect(viewModel.idOfertaRecusada) {
        if (viewModel.idOfertaRecusada != null) {
            ofertaARecusar = null
            idOfertaDetalhesAberto = null
            viewModel.limparDetalhesOferta()
            viewModel.limparEstadoRecusarOferta()
        }
    }

    LayoutPadrao(
        modifier = Modifier.fillMaxSize(),
        rotaAtual = "coletas",
        onNavigate = onNavigate,
        onLogout = onLogout,

        // Mostra o indicador enquanto as ofertas são carregadas
        atualizando = atualizacaoManual && viewModel.carregandoColetas,

        // Atualiza as ofertas ao puxar a tela para baixo
        onAtualizar = {
            if (!viewModel.carregandoColetas) {
                atualizacaoManual = true
                viewModel.carregarColetas()
            }
        }
    ) {
        ColetasScreen(
            viewModel = viewModel,
            onVerMais = { coleta ->
                viewModel.limparEstadoAceitarOferta()
                viewModel.limparEstadoRecusarOferta()

                idOfertaDetalhesAberto = coleta.idOfertaInicial

                viewModel.buscarDetalhesColeta(
                    coleta.idOfertaInicial
                )
            }
        )
    }

    // 1. Animação de sucesso
    if (viewModel.idOfertaAceita != null) {
        DialogSucessoEcoleta(
            mensagem = "A oferta foi aceita!",
            onConcluir = {
                idOfertaDetalhesAberto = null
                ofertaAConfirmar = null
                nomeFuncionarioAConfirmar = ""

                viewModel.limparEstadoAceitarOferta()
                viewModel.limparDetalhesOferta()
            }
        )

    // 2. Confirmação de aceite
    } else if (ofertaAConfirmar != null) {
        DialogConfirmarAceiteOferta(
            onNao = {
                ofertaAConfirmar = null
                nomeFuncionarioAConfirmar = ""
                idOfertaDetalhesAberto = null

                viewModel.limparEstadoAceitarOferta()
                viewModel.limparDetalhesOferta()
            },
            onSim = {
                ofertaAConfirmar?.let { oferta ->
                    viewModel.aceitarOferta(
                        idOferta = oferta.idOfertaInicial,
                        nomeFuncionario = nomeFuncionarioAConfirmar
                    )
                }
            },
            carregando = viewModel.aceitandoOferta,
            mensagemErro = viewModel.erroAceitarOferta
        )

    // 3. Confirmação de recusa
    } else if (ofertaARecusar != null) {
        DialogConfirmarAceiteOferta(
            modoRecusa = true,
            onNao = {
                // Fecha a confirmação e volta para Coletas
                ofertaARecusar = null
                idOfertaDetalhesAberto = null

                viewModel.limparEstadoRecusarOferta()
                viewModel.limparDetalhesOferta()
            },

            onSim = {
                ofertaARecusar?.let { oferta ->
                    viewModel.recusarOferta(
                        idOferta = oferta.idOfertaInicial
                    )
                }
            },

            carregando = viewModel.recusandoOferta,
            mensagemErro = viewModel.erroRecusarOferta
        )

    // 4. Detalhes da oferta
    } else if (idOfertaDetalhesAberto != null) {
        val idAberto = idOfertaDetalhesAberto

        val detalhes = viewModel.detalhesOferta?.takeIf {
            it.idOfertaInicial == idAberto
        }

        if (detalhes != null) {
            DialogDetalhesOferta(
                coleta = detalhes,
                onDismiss = {
                    if (!viewModel.recusandoOferta) {
                        idOfertaDetalhesAberto = null
                        viewModel.limparDetalhesOferta()
                        viewModel.limparEstadoRecusarOferta()
                    }
                },
                onAceitar = { oferta, nomeFuncionario ->
                    viewModel.limparEstadoAceitarOferta()
                    ofertaAConfirmar = oferta
                    nomeFuncionarioAConfirmar = nomeFuncionario
                },
                onRecusar = { oferta ->
                    viewModel.limparEstadoRecusarOferta()

                    // Guarda a oferta para solicitar confirmação
                    ofertaARecusar = oferta
                },
                carregandoOperacao = viewModel.recusandoOferta,
                mensagemErroOperacao = viewModel.erroRecusarOferta
            )

        } else if (viewModel.carregandoDetalhesOferta) {
            Dialog(
                onDismissRequest = {
                    idOfertaDetalhesAberto = null
                    viewModel.limparDetalhesOferta()
                }
            ) {
                CircularProgressIndicator(
                    color = VerdePrincipal
                )
            }

        } else {
            AlertDialog(
                onDismissRequest = {
                    idOfertaDetalhesAberto = null
                    viewModel.limparDetalhesOferta()
                },
                title = {
                    Text("Não foi possível abrir a oferta")
                },
                text = {
                    Text(
                        text = viewModel.erroDetalhesOferta?: "Os detalhes da oferta não estão disponíveis."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            idOfertaDetalhesAberto = null
                            viewModel.limparDetalhesOferta()
                        }
                    ) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}