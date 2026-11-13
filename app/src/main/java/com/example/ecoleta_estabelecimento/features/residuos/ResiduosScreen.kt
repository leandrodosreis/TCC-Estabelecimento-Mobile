package com.example.ecoleta_estabelecimento.features.residuos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ecoleta_estabelecimento.features.residuos.components.AnimacaoSucessoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.BotaoNovoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.CardResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.DialogNovoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.FiltrosResiduos
import com.example.ecoleta_estabelecimento.features.residuos.components.MensagemListaVazia
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel
import kotlinx.coroutines.delay

fun LazyListScope.ResiduosScreen(
    viewModel: ResiduosViewModel,
    onCancelarResiduo: (Residuo) -> Unit,
    onEditarResiduo: (Residuo) -> Unit,
    onVerColeta: (Residuo) -> Unit
) {

    // Títulos de Meus Resíduos
    item {

        // Responsável por controlar o foco do campo de busca
        val focusManager = LocalFocusManager.current

        // Controla se o popup de cadastro está aberto ou fechado
        var mostrarDialogNovoResiduo by remember {
            mutableStateOf(false)
        }

        // Controla se a animação de sucesso está visível
        var mostrarAnimacaoSucesso by remember {
            mutableStateOf(false)
        }

        // Fecha automaticamente a animação após três segundos
        LaunchedEffect(mostrarAnimacaoSucesso) {
            if (mostrarAnimacaoSucesso) {
                delay(2000)
                mostrarAnimacaoSucesso = false
            }
        }

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

            // Titulo da tela
            Text(
                text = "Meus resíduos",
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
                    Text("Buscar por material")
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
            FiltrosResiduos(
                // Dados e seleção do filtro por material
                tiposMateriais = viewModel.tiposMateriais,
                idTipoSelecionado = viewModel.idTipoSelecionado,
                onTipoSelecionado = viewModel::atualizarTipoSelecionado,

                // Dados e seleção do filtro por status
                statusDisponiveis = viewModel.statusDisponiveis,
                idStatusSelecionado = viewModel.idStatusSelecionado,
                onStatusSelecionado = viewModel::atualizarStatusSelecionado
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Chama o componente do Botão de adicionar novo resíduo
            BotaoNovoResiduo(
                onClick = {
                    // Abre o popup de cadastro de novo resíduo
                    mostrarDialogNovoResiduo = true
                }
            )
        }

        // Exibe o formulário de cadastro quando solicitado
        if (mostrarDialogNovoResiduo) {
            DialogNovoResiduo (
                viewModel = viewModel,
                // Fecha o popup ao clicar em Cancelar ou fora dele
                onDismiss = {
                    mostrarDialogNovoResiduo = false
                },
                // Executado somente quando o backend confirma o cadastro
                onCadastroSucesso = {
                    // Fecha o formulário
                    mostrarDialogNovoResiduo = false

                    // Abre a animação de confirmação
                    mostrarAnimacaoSucesso = true
                }
            )
        }

        // Mostra a animação Lottie sobre a tela de resíduos
        if (mostrarAnimacaoSucesso) {
            Dialog(
                // Não permite fechar tocando fora da animação
                onDismissRequest = {}
            ) {
                AnimacaoSucessoResiduo()
            }
        }
    }

    when {
        // Aguarda a consulta da API terminar
        viewModel.carregandoResiduos -> {
            item {
                Text(
                    text = "Carregando resíduos...",
                    color = subTitulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                )
            }
        }

        // Evita informar que a lista está vazia quando ocorreu um erro na consulta
        viewModel.erroCarregamentoResiduos != null -> {
            item {
                MensagemListaVazia(
                    titulo = "Não foi possível carregar os resíduos",
                    descricao = "Ocorreu um problema ao buscar seus resíduos. Tente novamente."
                )
            }
        }

        // O estabelecimento ainda não cadastrou resíduos
        viewModel.residuos.isEmpty() -> {
            item {
                MensagemListaVazia(
                    titulo = "Nenhum resíduo cadastrado",
                    descricao = "Você ainda não cadastrou resíduos. Clique em Novo resíduo para começar."
                )
            }
        }

        // Existem resíduos, mas a pesquisa não encontrou resultados
        viewModel.residuosFiltrados.isEmpty() && viewModel.busca.isNotBlank() -> {
            item {
                MensagemListaVazia(
                    titulo = "Nenhum resultado encontrado",
                    descricao = "Não encontramos resíduos para sua pesquisa. Tente buscar outro material."
                )
            }
        }

        // Existem resíduos, mas nenhum corresponde aos filtros
        viewModel.residuosFiltrados.isEmpty() -> {
            item {
                MensagemListaVazia(
                    titulo = "Nenhum resíduo encontrado",
                    descricao = "Nenhum resíduo corresponde aos filtros selecionados. Tente alterar o material ou status."
                )
            }
        }

        // Exibe normalmente os resíduos retornados pela API
        else -> {
            items(
                items = viewModel.residuosFiltrados,
                key = { residuo -> residuo.idResiduo }
            ) { residuo ->
                CardResiduo(
                    residuo = residuo,
                    onCancelar = {
                        onCancelarResiduo(residuo)
                    },
                    onEditar = {
                        onEditarResiduo(residuo)
                    },
                    onVerColeta = {
                        onVerColeta(residuo)
                    }
                )
            }
        }
    }
}