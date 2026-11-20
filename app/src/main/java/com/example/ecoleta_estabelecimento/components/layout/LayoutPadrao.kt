package com.example.ecoleta_estabelecimento.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.ecoleta_estabelecimento.components.loading.CarregamentoEcoleta
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutPadrao(
    modifier: Modifier = Modifier,
    rotaAtual: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    atualizando: Boolean = false,
    onAtualizar: () -> Unit = {},
    conteudo: LazyListScope.() -> Unit
) {

    // Gerencia o foco dos campos de texto presentes no conteúdo da tela
    val focusManager = LocalFocusManager.current

    // Controla o deslocamento e a animação do refresh
    val estadoRefresh = rememberPullToRefreshState()

    // Ordem das telas na barra inferior
    val rotas = listOf(
        "home",
        "coletas",
        "residuos",
        "historico",
        "perfil"
    )

    // Permite atualizar a tela puxando de cima para baixo
    PullToRefreshBox(
        isRefreshing = atualizando,
        onRefresh = onAtualizar,
        modifier = modifier.fillMaxSize(),
        state = estadoRefresh,
        indicator = {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(48.dp)
                    .pullToRefreshIndicator(
                        state = estadoRefresh,
                        isRefreshing = atualizando,
                        containerColor = BordaPadrao.copy(alpha = 0.85f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                CarregamentoEcoleta.Circular()
            }
        }
    ) {
        // Conteúdo rolável com navegação por gestos horizontais
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(VerdeFundo)
                // Observa os toques realizados em qualquer parte da lista, incluindo os cards e os espaços vazios entre os componentes
                .pointerInput(focusManager) {
                    awaitEachGesture {
                        awaitFirstDown(
                            requireUnconsumed = false,
                            pass = PointerEventPass.Initial
                        )

                        // Remove o foco sem consumir o toque
                        focusManager.clearFocus()
                    }
                }
                // Detecta o movimento horizontal do dedo
                .pointerInput(rotaAtual, onNavigate) {
                    // Distância mínima para trocar de tela
                    val distanciaMinima = 80.dp.toPx()

                    // Acumula o deslocamento horizontal
                    var deslocamentoHorizontal = 0f

                    detectHorizontalDragGestures(
                        // Início do gesto
                        onDragStart = {
                            deslocamentoHorizontal = 0f
                        },

                        // Enquanto o usuário desliza
                        onHorizontalDrag = { change, dragAmount ->

                            deslocamentoHorizontal += dragAmount

                            // Consome apenas o movimento horizontal
                            // depois que o gesto é reconhecido
                            change.consume()
                        },

                        // Quando o usuário solta o dedo
                        onDragEnd = {
                            val indiceAtual = rotas.indexOf(rotaAtual)

                            if (indiceAtual != -1) {
                                // Deslizou para a esquerda
                                // Próxima tela

                                if (
                                    deslocamentoHorizontal < -distanciaMinima
                                ) {

                                    val proximoIndice = indiceAtual + 1

                                    if (proximoIndice < rotas.size) {
                                        onNavigate(
                                            rotas[proximoIndice]
                                        )
                                    }
                                }

                                // Deslizou para a direita
                                // Tela anterior
                                else if (
                                    deslocamentoHorizontal > distanciaMinima
                                ) {

                                    val indiceAnterior = indiceAtual - 1

                                    if (indiceAnterior >= 0) {
                                        onNavigate(
                                            rotas[indiceAnterior]
                                        )
                                    }
                                }
                            }
                            deslocamentoHorizontal = 0f
                        },

                        // Reinicia se o gesto for cancelado
                        onDragCancel = {
                            deslocamentoHorizontal = 0f
                        }
                    )
                },
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 40.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Conteúdo específico da tela
            conteudo()
        }
    }
}