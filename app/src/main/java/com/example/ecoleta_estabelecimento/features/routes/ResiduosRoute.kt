package com.example.ecoleta_estabelecimento.features.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.ecoleta_estabelecimento.components.DialogSucessoEcoleta.DialogSucessoEcoleta
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosScreen
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogCancelarResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogDetalhesColeta
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogNovoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo

@Composable
fun ResiduosRoute(
    navController: NavHostController,
    viewModel: ResiduosViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {

    // Atualiza os resíduos sempre que entrar na tela
    LaunchedEffect(Unit) {
        viewModel.carregarResiduos()
    }

    var atualizacaoManual by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.carregandoResiduos) {
        if (!viewModel.carregandoResiduos) {
            atualizacaoManual = false
        }
    }
    // Resíduo selecionado para cancelamento
    var residuoParaCancelar by remember {
        mutableStateOf<Residuo?>(null)
    }

    // Resíduo selecionado para edição
    var residuoParaEditar by remember {
        mutableStateOf<Residuo?>(null)
    }

    // Resíduo selecionado para visualizar coleta
    var residuoParaVerColeta by remember {
        mutableStateOf<Residuo?>(null)
    }

    // Controle da animação de edição
    var mostrarAnimacaoEdicao by remember {
        mutableStateOf(false)
    }

    // Fecha o diálogo após cancelamento confirmado pela API
    LaunchedEffect(viewModel.idResiduoCancelado) {
        val idCancelado = viewModel.idResiduoCancelado

        if (idCancelado != null) {
            if (residuoParaCancelar?.idResiduo == idCancelado) {
                residuoParaCancelar = null
            }

            viewModel.limparEstadoCancelamentoResiduo()
        }
    }

    // Conteúdo da tela
    LayoutPadrao(
        modifier = Modifier.fillMaxSize(),
        rotaAtual = "residuos",
        onNavigate = onNavigate,
        onLogout = onLogout,

        // Mostra o indicador enquanto os resíduos são carregados
        atualizando = atualizacaoManual && viewModel.carregandoResiduos,

        // Atualiza a lista ao puxar a tela para baixo
        onAtualizar = {
            if (!viewModel.carregandoResiduos) {
                atualizacaoManual = true
                viewModel.carregarResiduos()
            }
        }
    ) {
        ResiduosScreen(
            viewModel = viewModel,

            onCancelarResiduo = { residuo ->
                viewModel.limparEstadoCancelamentoResiduo()
                residuoParaCancelar = residuo
            },

            onEditarResiduo = { residuo ->
                residuoParaEditar = residuo
            },

            onVerColeta = { residuo ->
                residuoParaVerColeta = residuo

                viewModel.buscarDetalhesColeta(
                    residuo.idResiduo
                )
            }
        )
    }

    // Diálogo de cancelamento
    residuoParaCancelar?.let { residuo ->
        DialogCancelarResiduo(
            residuo = residuo,
            viewModel = viewModel,

            onDismiss = {
                if (!viewModel.cancelandoResiduo) {
                    viewModel.limparEstadoCancelamentoResiduo()
                    residuoParaCancelar = null
                }
            },

            onConfirmar = { idResiduo ->
                viewModel.cancelarResiduo(idResiduo)
            }
        )
    }

    // Diálogo de edição
    residuoParaEditar?.let { residuo ->
        DialogNovoResiduo(
            residuoParaEditar = residuo,
            viewModel = viewModel,

            onDismiss = {
                if (!viewModel.editandoResiduo) {
                    residuoParaEditar = null
                }
            },

            onCadastroSucesso = {},

            onEdicaoSucesso = {
                residuoParaEditar = null
                mostrarAnimacaoEdicao = true
            }
        )
    }

    // Animação de sucesso da edição
    if (mostrarAnimacaoEdicao) {
        DialogSucessoEcoleta(
            mensagem = "Resíduo editado com sucesso!",
            onConcluir = {
                mostrarAnimacaoEdicao = false
            }
        )
    }

    // Diálogo com detalhes da coleta
    if (residuoParaVerColeta != null) {
        DialogDetalhesColeta(
            viewModel = viewModel,

            onDismiss = {
                residuoParaVerColeta = null
                viewModel.limparDetalhesColeta()
            }
        )
    }
}