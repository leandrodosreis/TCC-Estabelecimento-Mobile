package com.example.ecoleta_estabelecimento.features.historico.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoDropdownSelecionado
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoDropdownTextoSelecionado
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoFiltroTexto
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo


// Filtros do histórico
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FiltrosHistorico(
    tiposDisponiveis: List<String>,
    tipoSelecionado: String?,
    onTipoSelecionado: (String?) -> Unit,
    statusSelecionado: String?,
    onStatusSelecionado: (String?) -> Unit
) {

    // Estados dos dropdowns

    var menuTiposAberto by remember {
        mutableStateOf(false)
    }

    var menuStatusAberto by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val nomeTipoSelecionado = tipoSelecionado ?: "Tipos"
    val nomeStatusSelecionado = statusSelecionado ?: "Status"

    // Estrutura dos filtros
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Filtro por tipo
        FiltroDropdownHistorico(
            titulo = nomeTipoSelecionado,
            descricao = "Selecionar tipo",
            expandido = menuTiposAberto,
            onExpandir = {
                menuTiposAberto = !menuTiposAberto
                menuStatusAberto = false
            },
            onFechar = {
                menuTiposAberto = false
            },
            opcoes = listOf("Todos os tipos") + tiposDisponiveis.distinct(),
            opcaoSelecionada = tipoSelecionado ?: "Todos os tipos",
            onSelecionar = { opcao ->
                onTipoSelecionado(
                    if (opcao == "Todos os tipos") null else opcao
                )
                menuTiposAberto = false
            },
            modifier = Modifier.weight(1f)
        )

        // Filtro por status
        FiltroDropdownHistorico(
            titulo = nomeStatusSelecionado,
            descricao = "Selecionar status",
            expandido = menuStatusAberto,
            onExpandir = {
                menuStatusAberto = !menuStatusAberto
                menuTiposAberto = false
            },
            onFechar = {
                menuStatusAberto = false
            },
            opcoes = listOf(
                "Todos os status",
                "Concluída",
                "Cancelado"
            ),
            opcaoSelecionada = statusSelecionado ?: "Todos os status",
            onSelecionar = { opcao ->
                onStatusSelecionado(
                    if (opcao == "Todos os status") null else opcao
                )
                menuStatusAberto = false
            },
            modifier = Modifier.weight(1f)
        )

        // Botão Filtrar
        OutlinedButton(
            onClick = {
                menuTiposAberto = false
                menuStatusAberto = false
                focusManager.clearFocus()
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            shape = RoundedCornerShape(17.dp),
            border = BorderStroke(1.dp, BordaPadrao),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                contentColor = ResiduoFiltroTexto
            )
        ) {
            Text(
                text = "Filtrar",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Dropdown reutilizável
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FiltroDropdownHistorico(
    titulo: String,
    descricao: String,
    expandido: Boolean,
    onExpandir: () -> Unit,
    onFechar: () -> Unit,
    opcoes: List<String>,
    opcaoSelecionada: String,
    onSelecionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var larguraBotao by remember {
        mutableStateOf(0)
    }

    val density = LocalDensity.current

    val larguraBotaoDp = with(density) {
        larguraBotao.toDp()
    }

    Box(modifier = modifier) {

        // =====================
        // Botão do filtro
        // =====================

        OutlinedButton(
            onClick = onExpandir,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .onGloballyPositioned { coordinates ->
                    larguraBotao = coordinates.size.width
                },
            shape = RoundedCornerShape(
                topStart = 17.dp,
                topEnd = 17.dp,
                bottomStart = if (expandido) 0.dp else 17.dp,
                bottomEnd = if (expandido) 0.dp else 17.dp
            ),
            border = BorderStroke(1.dp, BordaPadrao),
            contentPadding = PaddingValues(horizontal = 4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Branco,
                contentColor = ResiduoFiltroTexto
            )
        ) {

            Text(
                text = titulo,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        initialDelayMillis = 1000,
                        repeatDelayMillis = 1000,
                        velocity = 30.dp
                    )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = descricao,
                tint = subTitulo,
                modifier = Modifier.size(26.dp)
            )
        }

        // Menu de opções
        if (expandido && larguraBotao > 0) {

            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(
                    x = 0,
                    y = with(density) { 60.dp.roundToPx() }
                ),
                onDismissRequest = onFechar,
                properties = PopupProperties(
                    focusable = true
                )
            ) {
                Column(
                    modifier = Modifier
                        .width(larguraBotaoDp)
                        .heightIn(max = 350.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 17.dp,
                                bottomEnd = 17.dp
                            )
                        )
                        .background(Branco)
                        .verticalScroll(
                            androidx.compose.foundation.rememberScrollState()
                        )
                ) {

                    opcoes.forEach { opcao ->

                        val selecionada = opcao == opcaoSelecionada

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (selecionada) {
                                        ResiduoDropdownSelecionado
                                    } else {
                                        Branco
                                    }
                                )
                                .clickable {
                                    onSelecionar(opcao)
                                }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 16.dp
                                )
                        ) {

                            Text(
                                text = opcao,
                                fontSize = 14.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Clip,
                                modifier = Modifier.basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    initialDelayMillis = 1000,
                                    repeatDelayMillis = 1000,
                                    velocity = 30.dp
                                ),
                                color = if (selecionada) {
                                    ResiduoDropdownTextoSelecionado
                                } else {
                                    ResiduoFiltroTexto
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}