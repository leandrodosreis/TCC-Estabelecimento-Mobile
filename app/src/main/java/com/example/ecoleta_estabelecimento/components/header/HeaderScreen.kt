package com.example.ecoleta_estabelecimento.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HeaderDivisoria
import com.example.ecoleta_estabelecimento.ui.theme.HeaderSelecionado
import com.example.ecoleta_estabelecimento.ui.theme.HeaderSombra
import com.example.ecoleta_estabelecimento.ui.theme.HeaderTexto
import com.example.ecoleta_estabelecimento.ui.theme.Preto
import com.example.ecoleta_estabelecimento.ui.theme.Transparente

// Representa uma opção do menu de navegação
private data class ItemMenu(
    val nome: String,
    val rota: String,
    val icone: Int
)

// Lista das opções exibidas no menu
private val itens = listOf(
    ItemMenu(nome = "Home",          rota = "home",      icone = R.drawable.icone_home),
    ItemMenu(nome = "Meus Resíduos", rota = "residuos",  icone = R.drawable.icone_residuos),
    ItemMenu(nome = "Coletas",       rota = "coletas",   icone = R.drawable.icone_coletas),
    ItemMenu(nome = "Histórico",     rota = "historico", icone = R.drawable.icone_historico),
    ItemMenu(nome = "Perfil",        rota = "perfil",    icone = R.drawable.icone_perfil)
)

// Posiciona o menu na lateral direita, cobrindo a sombra do Header
private class MenuPositionProvider(
    private val alturaSombra: Int
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {

        // Alinha o menu à lateral direita
        val posicaoX = windowSize.width - popupContentSize.width

        // Posiciona o menu sobre a sombra inferior
        val posicaoY = anchorBounds.bottom - alturaSombra

        return IntOffset(
            x = posicaoX,
            y = posicaoY
        )
    }
}

@Composable
fun HeaderScreen(
    modifier: Modifier = Modifier,
    rotaAtual: String = "home",
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {

    // Controla se o menu está aberto ou fechado
    var aberto by remember {
        mutableStateOf(false)
    }

    // Converte a altura da sombra para pixels
    val alturaSombra = with(LocalDensity.current) {
        5.dp.roundToPx()
    }

    // Agrupa o Header, a sombra e o menu
    Box(
        modifier = modifier.fillMaxWidth()
    ) {

        // Organiza o cabeçalho e a sombra verticalmente
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // Cria a área branca do cabeçalho
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Branco,
                shadowElevation = 0.dp
            ) {

                // Organiza a logo à esquerda e o botão à direita
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Exibe a logo do Ecoleta
                    Image(
                        painter = painterResource(
                            id = R.drawable.logo_ecoleta
                        ),
                        contentDescription = "Logo do Ecoleta",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(113.dp)
                            .height(30.dp)
                            .clickable {

                                // Fecha o menu e seleciona a Home
                                aberto = false
                                onNavigate("home")
                            }
                    )

                    // Botão responsável por abrir e fechar o menu
                    IconButton(
                        onClick = {
                            aberto = !aberto
                        }
                    ) {

                        Text(
                            text = "☰",
                            fontSize = 30.sp,
                            color = Preto
                        )
                    }
                }
            }

            // Cria a sombra somente abaixo do Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                HeaderSombra,
                                Transparente
                            )
                        )
                    )
            )
        }

        // Exibe o menu quando aberto for true
        if (aberto) {

            Popup(
                // Posiciona o menu com base no Header completo
                popupPositionProvider = MenuPositionProvider(alturaSombra),

                // Fecha o menu ao tocar fora dele
                onDismissRequest = {
                    aberto = false
                },

                properties = PopupProperties(
                    focusable = true
                )
            ) {

                // Cria um único card branco, sem sombra
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 0.dp
                            )
                        )
                        .background(Branco)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                ) {

                    // Título exibido na parte superior do menu
                    Text(
                        text = "Navegação",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HeaderTexto,
                        modifier = Modifier.padding(
                            bottom = 12.dp
                        )
                    )

                    // Percorre a lista e cria os itens de navegação
                    itens.forEach { item ->

                        // Verifica se esta opção é a tela atual
                        val selecionado = rotaAtual == item.rota

                        // Representa uma opção clicável do menu
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)

                                // Destaca a opção selecionada
                                .background(
                                    color = if (selecionado)
                                        HeaderSelecionado
                                    else
                                        Transparente,
                                    shape = RoundedCornerShape(6.dp)
                                )

                                // Fecha o menu e informa a rota escolhida
                                .clickable {
                                    aberto = false
                                    onNavigate(item.rota)
                                }
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 10.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Exibe o ícone da opção
                            Image(
                                painter = painterResource(
                                    id = item.icone
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )

                            // Espaçamento entre o ícone e o nome
                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )

                            // Exibe o nome da opção
                            Text(
                                text = item.nome,
                                fontSize = 13.sp,
                                color = HeaderTexto
                            )
                        }
                    }

                    // Separa as opções do botão Sair
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = HeaderDivisoria
                    )

                    // Botão responsável pela saída do aplicativo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                // Fecha o menu e executa a saída
                                aberto = false
                                onLogout()
                            }
                            .padding(
                                horizontal = 8.dp,
                                vertical = 10.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Ícone de saída
                        Image(
                            painter = painterResource(
                                id = R.drawable.icone_sair
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        // Texto do botão Sair
                        Text(
                            text = "Sair",
                            fontSize = 13.sp,
                            color = HeaderTexto
                        )
                    }
                }
            }
        }
    }
}