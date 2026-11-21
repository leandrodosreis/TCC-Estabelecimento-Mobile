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
import com.example.ecoleta_estabelecimento.features.historico.HistoricoScreen
import com.example.ecoleta_estabelecimento.features.historico.HistoricoViewModel
import com.example.ecoleta_estabelecimento.features.historico.components.DialogDetalhesHistorico
import com.example.ecoleta_estabelecimento.features.historico.model.HistoricoEstabelecimento

@Composable
fun HistoricoRoute(
    viewModel: HistoricoViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    // Controla se a atualização foi iniciada manualmente
    var atualizacaoManual by remember {
        mutableStateOf(false)
    }

    // Finaliza o indicador após o carregamento
    LaunchedEffect(viewModel.carregandoHistorico) {
        if (!viewModel.carregandoHistorico) {
            atualizacaoManual = false
        }
    }

    // Registro selecionado para visualizar detalhes
    var registroParaVerDetalhes by remember {
        mutableStateOf<HistoricoEstabelecimento?>(null)
    }

    // Conteúdo da tela
    LayoutPadrao(
        modifier = Modifier.fillMaxSize(),
        rotaAtual = "historico",
        onNavigate = onNavigate,
        onLogout = onLogout,
        // Mostra o indicador enquanto o histórico é atualizado
        atualizando = atualizacaoManual && viewModel.carregandoHistorico,
        // Atualiza o histórico ao puxar a tela para baixo
        onAtualizar = {
            if (!viewModel.carregandoHistorico) {
                atualizacaoManual = true
                viewModel.carregarHistorico()
            }
        }
    ) {
        HistoricoScreen(
            viewModel = viewModel,
            onVerMais = { registro ->
                registroParaVerDetalhes = registro
                viewModel.buscarDetalhesHistorico(registro)
            }
        )
    }

    // Diálogo com detalhes do histórico
    if (registroParaVerDetalhes != null) {
        DialogDetalhesHistorico(
            detalhes = viewModel.detalhesHistorico,
            carregando = viewModel.carregandoDetalhesHistorico,
            mensagemErro = viewModel.erroDetalhesHistorico,
            onDismiss = {
                registroParaVerDetalhes = null
                viewModel.limparDetalhesHistorico()
            }
        )
    }
}