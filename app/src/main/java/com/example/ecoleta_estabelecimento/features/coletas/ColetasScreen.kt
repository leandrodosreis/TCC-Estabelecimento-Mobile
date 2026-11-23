package com.example.ecoleta_estabelecimento.features.coletas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.components.loading.CarregamentoEcoleta
import com.example.ecoleta_estabelecimento.components.paginacao.ComponentePaginacao
import com.example.ecoleta_estabelecimento.features.coletas.components.CardColeta
import com.example.ecoleta_estabelecimento.features.coletas.components.FiltroColetas
import com.example.ecoleta_estabelecimento.features.coletas.model.Coleta
import com.example.ecoleta_estabelecimento.features.residuos.components.MensagemListaVazia
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.ColetasScreen(
    viewModel: ColetasViewModel,
    onVerMais: (Coleta) -> Unit
) {
    item {
        Column {
            Text(
                text = "PAINEL DO ESTABELECIMENTO",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = textoPainel
            )

            // Espaçamento entre título e Nome da Tela
            Spacer(modifier = Modifier.height(10.dp))

            // Titulo da tela
            Text(
                text = "Coletas",
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                color = nomeTela
            )

            // Espaçamento entre Nome da Tela e subTitulo
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitulo
            Text(
                text = "Gerencie e acompanhe suas operações na Ecoleta.",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = subTitulo
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // Filtros e ofertas
    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Mostra os filtros apenas quando os dados estão disponíveis
            if ((viewModel.coletas.isNotEmpty() || !viewModel.carregandoColetas) && viewModel.erroCarregamentoColetas == null) {
                // Filtro Todas / Pendentes / Aceitas
                FiltroColetas(
                    filtroSelecionado = viewModel.filtroSelecionado,
                    onFiltroSelecionado = { novoFiltro ->
                        viewModel.atualizarFiltroComPaginacao(novoFiltro)
                    }
                )
            }

            // Carregamento inicial
            if (viewModel.carregandoColetas && viewModel.coletas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CarregamentoEcoleta.Circular()
                }
            } else if (viewModel.erroCarregamentoColetas != null) {
                // Mensagem de erro padronizada
                MensagemListaVazia(
                    titulo = "Não foi possível carregar as coletas",
                    descricao = "Ocorreu um problema ao buscar suas coletas. Tente novamente."
                )
            } else {
                // Ofertas pendentes
                if (viewModel.filtroSelecionado == "Todas" || viewModel.filtroSelecionado == "Pendentes") {
                    SecaoColetas(
                        titulo = "Ofertas pendentes",
                        subtitulo = "ATENÇÃO NECESSÁRIA",
                        coletas = viewModel.ofertasPendentesPaginadas,
                        paginaAtual = viewModel.paginaAtualPendentes,
                        totalPaginas = viewModel.totalPaginasPendentes,
                        onPaginaSelecionada = { novaPagina ->
                            viewModel.mudarPaginaPendentes(novaPagina)
                        },
                        onVerMais = onVerMais
                    )
                }

                // Ofertas aceitas
                if (viewModel.filtroSelecionado == "Todas" || viewModel.filtroSelecionado == "Aceitas") {
                    SecaoColetas(
                        titulo = "Ofertas aceitas",
                        subtitulo = "OFERTAS CONFIRMADAS",
                        coletas = viewModel.ofertasAceitasPaginadas,
                        paginaAtual = viewModel.paginaAtualAceitas,
                        totalPaginas = viewModel.totalPaginasAceitas,
                        onPaginaSelecionada = { novaPagina ->
                            viewModel.mudarPaginaAceitas(novaPagina)
                        },
                        onVerMais = onVerMais
                    )
                }
            }
        }
    }
}

// Seção de ofertas
@Composable
private fun SecaoColetas(
    titulo: String,
    subtitulo: String,
    coletas: List<Coleta>,
    onVerMais: (Coleta) -> Unit,
    paginaAtual: Int,
    totalPaginas: Int,
    onPaginaSelecionada: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = subtitulo,
            color = VerdePrincipal,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = titulo,
            color = nomeTela,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }

    // Cards ou mensagem de lista vazia
    if (coletas.isEmpty()) {
        Text(
            text = if (titulo == "Ofertas pendentes") {
                "Nenhuma oferta pendente para seus resíduos."
            } else {
                "Você ainda não possui ofertas aceitas."
            },
            color = HomeTextoSecundario,
            fontSize = 13.sp
        )
    } else {
        coletas.forEach { coleta ->
            CardColeta(
                coleta = coleta,
                onVerMais = {
                    onVerMais(coleta)
                }
            )
        }

        // Paginação exclusiva desta seção (aparece se essa seção tiver mais de 5 itens)
        if (totalPaginas > 1) {
            Spacer(modifier = Modifier.height(6.dp))
            ComponentePaginacao(
                paginaAtual = paginaAtual,
                totalPaginas = totalPaginas,
                onPaginaSelecionada = onPaginaSelecionada
            )
        }
    }
}