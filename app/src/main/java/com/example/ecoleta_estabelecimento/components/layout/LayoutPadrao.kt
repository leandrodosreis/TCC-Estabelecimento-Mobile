package com.example.ecoleta_estabelecimento.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.ecoleta_estabelecimento.components.header.HeaderScreen
import com.example.ecoleta_estabelecimento.components.navigation.BarraNavegacaoInferior
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo

@Composable
fun LayoutPadrao(
    modifier: Modifier = Modifier,
    rotaAtual: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    conteudo: LazyListScope.() -> Unit
) {

    // Gerencia o foco dos campos de texto presentes no conteúdo da tela
    val focusManager = LocalFocusManager.current

    // Ordem das telas na barra inferior
    val rotas = listOf(
        "home",
        "coletas",
        "residuos",
        "historico",
        "perfil"
    )

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