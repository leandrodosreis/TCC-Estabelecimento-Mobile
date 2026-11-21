package com.example.ecoleta_estabelecimento.features.historico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.components.loading.CarregamentoEcoleta
import com.example.ecoleta_estabelecimento.features.historico.components.CardHistorico
import com.example.ecoleta_estabelecimento.features.historico.components.FiltrosHistorico
import com.example.ecoleta_estabelecimento.features.historico.components.TipoHistorico
import com.example.ecoleta_estabelecimento.features.historico.model.HistoricoEstabelecimento
import com.example.ecoleta_estabelecimento.features.residuos.components.MensagemListaVazia
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.HistoricoScreen(
    viewModel: HistoricoViewModel,
    onVerMais: (HistoricoEstabelecimento) -> Unit
) {
    // Títulos do Histórico
    item {
        // Responsável por controlar o foco do campo de busca
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Detecta quando o usuário toca em uma área vazia da Column
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // Remove o foco do campo de busca
                    // A borda deixa de ficar verde e volta à cor padrão
                    focusManager.clearFocus()
                }
        ) {
            Text(
                text = "PAINEL DO ESTABELECIMENTO",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = textoPainel
            )

            // Espaçamento entre título e Nome da Tela
            Spacer(modifier = Modifier.height(10.dp))

            // Título da tela
            Text(
                text = "Histórico de coletas",
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                color = nomeTela
            )

            // Espaçamento entre Nome da Tela e subTitulo
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitulo
            Text(
                text = "Consulte suas coletas concluídas e resíduos cancelados.",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = subTitulo
            )

            // Exibe os controles somente quando não houver erro na API
            if (viewModel.registros.isNotEmpty() && viewModel.erroCarregamentoHistorico == null) {
                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = viewModel.busca,
                    onValueChange = { novoTexto ->
                        viewModel.atualizarBusca(novoTexto)
                    },
                    // Define o botão de ação do teclado como Buscar
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    // Ao pressionar Buscar, remove o foco e fecha o teclado
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Buscar por empresa ou material")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Pesquisar"
                        )
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdePrincipal,
                        unfocusedBorderColor = BordaPadrao,
                        focusedContainerColor = Branco,
                        unfocusedContainerColor = Branco
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Chama os componentes para o filtro
                FiltrosHistorico(
                    // Dados e seleção do filtro por material
                    tiposDisponiveis = viewModel.tiposDisponiveis,
                    tipoSelecionado = viewModel.tipoSelecionado,
                    onTipoSelecionado = viewModel::atualizarTipoSelecionado,

                    // Dados e seleção do filtro por status
                    statusSelecionado = viewModel.statusSelecionado,
                    onStatusSelecionado = viewModel::atualizarStatusSelecionado
                )
            }
        }
    }

    when {
        viewModel.carregandoHistorico -> {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CarregamentoEcoleta.Circular()
                }
            }
        }

        // Evita informar que a lista está vazia quando ocorreu um erro na consulta
        viewModel.erroCarregamentoHistorico != null -> {
            item {
                MensagemListaVazia(
                    titulo = "Não foi possível carregar o histórico",
                    descricao = "Ocorreu um problema ao buscar seu histórico. Tente novamente."
                )
            }
        }

        // O estabelecimento ainda não possui registros no histórico
        viewModel.registros.isEmpty() -> {
            item {
                MensagemListaVazia(
                    titulo = "Nenhum histórico encontrado",
                    descricao = "Você ainda não possui coletas concluídas ou resíduos cancelados."
                )
            }
        }

        // Existem registros, mas a pesquisa não encontrou resultados
        viewModel.registrosFiltrados.isEmpty() && viewModel.busca.isNotBlank() -> {
            item {
                MensagemListaVazia(
                    titulo = "Nenhum resultado encontrado",
                    descricao = "Não encontramos registros para sua pesquisa. Tente buscar outra empresa ou material."
                )
            }
        }

        // Existem registros, mas nenhum corresponde aos filtros
        viewModel.registrosFiltrados.isEmpty() -> {
            item {
                MensagemListaVazia(
                    titulo = "Nenhum histórico encontrado",
                    descricao = "Nenhum registro corresponde aos filtros selecionados. Tente alterar o material ou status."
                )
            }
        }

        // Exibe normalmente os registros retornados pela API
        else -> {
            items(
                items = viewModel.registrosFiltrados,
                key = { registro ->
                    if (registro.idColeta != null) {
                        "coleta_${registro.idColeta}"
                    } else {
                        "residuo_${registro.idResiduo}"
                    }
                }
            ) { registro ->

                // Identifica se o registro é uma coleta concluída ou resíduo cancelado
                val tipo = if (registro.idColeta != null) {
                    TipoHistorico.CONCLUIDA
                } else {
                    TipoHistorico.CANCELADO
                }

                // Exibe a empresa coletora nas coletas concluídas
                // Exibe o estabelecimento nos resíduos cancelados
                val titulo = if (tipo == TipoHistorico.CONCLUIDA) {
                    registro.nomeEmpresaColetora ?: "Empresa coletora"
                } else {
                    registro.nomeEstabelecimento
                }

                CardHistorico(
                    titulo = titulo,
                    material = registro.material,
                    quantidade = registro.quantidade,
                    data = registro.dataDisponivel,
                    tipo = tipo,
                    valor = registro.valorAcordado,
                    onVerMais = {
                        onVerMais(registro)
                    }
                )
            }
        }
    }
}