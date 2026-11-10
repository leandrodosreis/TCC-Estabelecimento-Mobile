package com.example.ecoleta_estabelecimento.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.ecoleta_estabelecimento.components.footer.FooterScreen
import com.example.ecoleta_estabelecimento.components.header.HeaderScreen
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

    // Organiza o Header, conteúdo e Footer
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Branco)
    ) {

        // Header global fixo
        HeaderScreen(
            rotaAtual = rotaAtual,
            onNavigate = onNavigate,
            onLogout = onLogout
        )

        // Conteúdo rolável + Footer
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(VerdeFundo)
            // Observa os toques realizados em qualquer parte da lista,
            // incluindo os cards e os espaços vazios entre os componentes
                .pointerInput(focusManager) {
                    awaitPointerEventScope {
                        while (true) {
                            // Observa o toque antes dos componentes filhos, sem consumir o evento e sem bloquear seus cliques
                            val evento = awaitPointerEvent(PointerEventPass.Initial)

                            // Verifica se o usuário acabou de encostar na tela
                            if (evento.changes.any { it.changedToDownIgnoreConsumed() }) {

                                // Remove o foco do campo de texto ativo
                                focusManager.clearFocus()
                            }
                        }
                    }
                },
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 40.dp,
                bottom = 0.dp
            ),
            verticalArrangement = EspacamentoComFooter
        ) {

            // Conteúdo específico da tela
            conteudo()

            // Footer com espaçamento extra
            item {
                Column {
                    Spacer(modifier = Modifier.height(35.dp))

                    FooterScreen()
                }
            }
        }
    }
}

private val EspacamentoComFooter = object : Arrangement.Vertical {

    override val spacing = 20.dp

    // Calcula onde cada item deve ficar
    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray
    ) {
        val espacamento = 20.dp.roundToPx() // Calculando o espaço ocupado
        val alturaItens = sizes.sum() // Soma as alturas de todos os itens
        // Calcula o espaço necessário entre os itens, .coerceAtLeast(0) garante que o resultado nunca seja negativo, inclusive se não houver nenhum item
        val alturaEspacamentos = espacamento * (sizes.size - 1).coerceAtLeast(0)

        // Faz o Footer descer
        val espacoLivre = (
                totalSize - alturaItens - alturaEspacamentos
                ).coerceAtLeast(0)

        var posicao = 0

        // index: posição do item no array, começando em zero
        // tamanho: altura do item atual, em pixels
        sizes.forEachIndexed { index, tamanho ->
            // Adiciona o espaço livre somente antes do Footer
            // Responsável por empurrar o Footer para baixo
            if (index == sizes.lastIndex) {
                posicao += espacoLivre
            }

            // Adiciona todo o espaço livre à posição, ou seja, onde o Compose deve posicionar aquele item
            outPositions[index] = posicao
            posicao += tamanho + espacamento
        }
    }
}