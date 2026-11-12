package com.example.ecoleta_estabelecimento.features.residuos.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.ecoleta_estabelecimento.features.residuos.model.TipoMaterial
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoDropdownSelecionado
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoDropdownTextoSelecionado
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoFiltroTexto
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

// Permite utilizar o efeito de rolagem horizontal do texto
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FiltrosResiduos(
    // Filtro por tipo de material
    tiposMateriais: List<TipoMaterial>,
    idTipoSelecionado: Int?,
    onTipoSelecionado: (Int?) -> Unit,

    // Filtro por status: cada opção contém o ID e o nome
    statusDisponiveis: List<Pair<Int, String>>,
    idStatusSelecionado: Int?,
    onStatusSelecionado: (Int?) -> Unit
) {
    var menuTiposAberto by remember {
        mutableStateOf(false)
    }

    val nomeTipoSelecionado = tiposMateriais
        .find { it.idTipoMaterial == idTipoSelecionado }
        ?.material ?: "Tipos"

    // Permite que o menu tenha exatamente a mesma largura do botão Tipos
    var larguraBotao by remember {
        mutableStateOf(0)
    }

    val density = LocalDensity.current

    val larguraBotaoDp = with(density) {
        larguraBotao.toDp()
    }

    // Controla se o dropdown de Status está aberto ou fechado
    var menuStatusAberto by remember {
        mutableStateOf(false)
    }
    // Descobre o nome do status selecionado para exibir no botão
    // Se nenhum status estiver selecionado, exibe "Status"
    val nomeStatusSelecionado = statusDisponiveis
        .find { it.first == idStatusSelecionado }
        ?.second ?: "Status"

    // Guarda a largura real do botão Status em pixels
    var larguraBotaoStatus by remember {
        mutableStateOf(0)
    }

    // Converte a largura para dp, para usar no dropdown
    val larguraBotaoStatusDp = with(density) {
        larguraBotaoStatus.toDp()
    }

    // Permite remover o foco do campo de busca ao clicar em Filtrar
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {
            // Filtro por tipo de material
            OutlinedButton(
                onClick = {
                    // Abre ou fecha o menu de Tipos
                    menuTiposAberto = !menuTiposAberto

                    // Fecha o menu de Status caso esteja aberto
                    menuStatusAberto = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .onGloballyPositioned { coordinates ->
                        larguraBotao = coordinates.size.width
                    },
                shape = RoundedCornerShape(
                    topStart = 17.dp,
                    topEnd = 17.dp,
                    bottomStart = if (menuTiposAberto) 0.dp else 17.dp,
                    bottomEnd = if (menuTiposAberto) 0.dp else 17.dp
                ),
                border = BorderStroke(1.dp, BordaPadrao),
                contentPadding = PaddingValues(horizontal = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Branco,
                    contentColor = ResiduoFiltroTexto
                )
            ) {
                Text(
                    text = nomeTipoSelecionado,
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

                // Espaçamento entre o texto e a seta
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Selecionar tipo",
                    tint = subTitulo,
                    modifier = Modifier.size(26.dp)
                )
            }

            if (menuTiposAberto && larguraBotao > 0) {
                Popup(
                    alignment = Alignment.TopStart,

                    // Posiciona o menu logo abaixo do botão Tipos, que possui 60.dp de altura
                    offset = IntOffset(
                        x = 0,
                        y = with(density) { 60.dp.roundToPx() }
                    ),

                    // Fecha o menu quando o usuário clicar fora dele
                    onDismissRequest = {
                        menuTiposAberto = false
                    },
                    properties = PopupProperties(
                        focusable = true
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .width(larguraBotaoDp)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 17.dp,
                                    bottomEnd = 17.dp
                                )
                            )
                            .background(Branco)
                    ) {
                        // Todos os tipos
                        val todosSelecionado = idTipoSelecionado == null

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (todosSelecionado)
                                        ResiduoDropdownSelecionado
                                    else
                                        Branco
                                )
                                .clickable {
                                    onTipoSelecionado(null)
                                    menuTiposAberto = false
                                }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 16.dp
                                )
                        ) {
                            Text(
                                text = "Todos os tipos",
                                fontSize = 14.sp,
                                // Mantém o texto em uma única linha
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Clip,

                                // Faz o texto deslizar horizontalmente quando não cabe no espaço
                                // O movimento acontece automaticamente enquanto o texto estiver visível
                                modifier = Modifier.basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    initialDelayMillis = 1000,
                                    repeatDelayMillis = 1000,
                                    velocity = 30.dp
                                ),
                                color = if (idTipoSelecionado == null)
                                    ResiduoDropdownTextoSelecionado
                                else
                                    ResiduoFiltroTexto
                            )
                        }

                        // Opções de materiais recebidas da ViewModel
                        tiposMateriais.forEach { tipo ->

                            val selecionado = idTipoSelecionado == tipo.idTipoMaterial

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (selecionado)
                                            ResiduoDropdownSelecionado
                                        else
                                            Branco
                                    )
                                    .clickable {
                                        onTipoSelecionado(tipo.idTipoMaterial)
                                        menuTiposAberto = false
                                    }
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 16.dp
                                    )
                            ) {
                                Text(
                                    text = tipo.material,
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
                                    color = if (selecionado)
                                        ResiduoDropdownTextoSelecionado
                                    else
                                        ResiduoFiltroTexto
                                )
                            }
                        }
                    }
                }
            }
        }

        // ==================
        // Filtro por status
        // ==================
        Box(
            modifier = Modifier.weight(1f)
        ) {
            // Botão responsável por abrir e fechar o dropdown de Status
            OutlinedButton(
                onClick = {
                    // Abre ou fecha o menu de Status
                    menuStatusAberto = !menuStatusAberto

                    // Fecha o menu de Tipos para não deixar os dois abertos
                    menuTiposAberto = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .onGloballyPositioned { coordinates ->
                        // Obtém a largura real do botão para alinhar o dropdown
                        larguraBotaoStatus = coordinates.size.width
                    },
                shape = RoundedCornerShape(
                    topStart = 17.dp,
                    topEnd = 17.dp,

                    // Quando o menu está aberto, remove os cantos inferiores
                    // para criar a aparência de continuidade com o dropdown
                    bottomStart = if (menuStatusAberto) 0.dp else 17.dp,
                    bottomEnd = if (menuStatusAberto) 0.dp else 17.dp
                ),
                border = BorderStroke(1.dp, BordaPadrao),
                contentPadding = PaddingValues(horizontal = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Branco,
                    contentColor = ResiduoFiltroTexto
                )
            ) {
                // Mostra o nome do status selecionado ou "Status"
                Text(
                    text = nomeStatusSelecionado,
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

                // Espaçamento entre o texto e a seta
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Selecionar status",
                    tint = subTitulo,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Exibe o dropdown somente quando o botão estiver aberto e sua largura já tiver sido calculada
            if (menuStatusAberto && larguraBotaoStatus > 0) {
                Popup(
                    alignment = Alignment.TopStart,
                    // Posiciona o dropdown logo abaixo do botão de 60.dp
                    offset = IntOffset(
                        x = 0,
                        y = with(density) { 60.dp.roundToPx() }
                    ),
                    // Fecha o dropdown ao tocar fora dele
                    onDismissRequest = {
                        menuStatusAberto = false
                    },

                    properties = PopupProperties(
                        focusable = true
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .width(larguraBotaoStatusDp)
                            // Arredonda apenas os cantos inferiores do dropdown
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 17.dp,
                                    bottomEnd = 17.dp
                                )
                            )
                            .background(Branco)
                    ) {
                        // Verifica se a opção "Todos os status" está selecionada
                        val todosStatusSelecionados = idStatusSelecionado == null

                        // Primeira opção: remove o filtro por status
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (todosStatusSelecionados)
                                        ResiduoDropdownSelecionado
                                    else
                                        Branco
                                )
                                .clickable {
                                    // null significa que todos os status serão exibidos
                                    onStatusSelecionado(null)

                                    // Fecha o dropdown após a seleção
                                    menuStatusAberto = false
                                }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 16.dp
                                )
                        ) {
                            Text(
                                text = "Todos os status",
                                fontSize = 14.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Clip,
                                // Faz o texto rolar horizontalmente se não couber
                                modifier = Modifier.basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    initialDelayMillis = 1000,
                                    repeatDelayMillis = 1000,
                                    velocity = 30.dp
                                ),
                                // Destaca a opção selecionada com texto branco
                                color = if (todosStatusSelecionados)
                                    ResiduoDropdownTextoSelecionado
                                else
                                    ResiduoFiltroTexto
                            )
                        }

                        // Percorre os status disponíveis na ViewModel
                        statusDisponiveis.forEach { (idStatus, nomeStatus) ->
                            // Verifica se este status é o selecionado atualmente
                            val selecionado = idStatusSelecionado == idStatus

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (selecionado)
                                            ResiduoDropdownSelecionado
                                        else
                                            Branco
                                    )
                                    .clickable {
                                        // Envia o ID do status selecionado para a ViewModel
                                        onStatusSelecionado(idStatus)

                                        // Fecha o dropdown após a seleção
                                        menuStatusAberto = false
                                    }
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 16.dp
                                    )
                            ) {
                                Text(
                                    text = nomeStatus,
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
                                    color = if (selecionado)
                                        ResiduoDropdownTextoSelecionado
                                    else
                                        ResiduoFiltroTexto
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botão para aplicar os filtros
        OutlinedButton(
            onClick = {
                // Fecha os dois dropdowns, caso algum esteja aberto
                menuTiposAberto = false
                menuStatusAberto = false
                // Remove o foco do campo de busca e fecha o teclado
                focusManager.clearFocus()
                // Os resíduos já são filtrados automaticamente pela ViewModel
                // usando a busca, o tipo e o status selecionados
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