package com.example.ecoleta_estabelecimento.components.navigation

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

// Botões laterais da navegação
@Composable
private fun ItemNavegacaoInferior(
    titulo: String,
    selecionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconeVetorial: ImageVector? = null,
    @DrawableRes iconeDrawable: Int? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                color = if (selecionado)
                    VerdeFundo
                else
                    Branco
            )
            .border(
                width = if (selecionado) 1.5.dp else 0.dp,
                color = if (selecionado)
                    VerdePrincipal
                else
                    Branco,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = 2.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Usa o ícone vetorial quando disponível
        if (iconeVetorial != null) {
            Icon(
                imageVector = iconeVetorial,
                contentDescription = titulo,
                tint = if (selecionado)
                    VerdePrincipal
                else
                    HomeTextoSecundario,
                modifier = Modifier.size(23.dp)
            )
        }
        // Caso contrário, utiliza a imagem do drawable
        else if (iconeDrawable  != null) {
            Image(
                painter = painterResource(id = iconeDrawable),
                contentDescription = titulo,
                modifier = Modifier.size(23.dp),
                colorFilter = ColorFilter.tint(
                    if (selecionado)
                        VerdePrincipal
                    else
                        HomeTextoSecundario
                )
            )
        }

        // Nome da tela
        Text(
            text = titulo,
            fontSize = 10.sp,
            fontWeight = if (selecionado)
                FontWeight.Bold
            else
                FontWeight.Medium,
            color = if (selecionado)
                VerdePrincipal
            else
                HomeTextoSecundario,
            maxLines = 1
        )
    }
}

// Botão principal de Meus Resíduos
@Composable
private fun BotaoCentralNavegacao(
    selecionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .offset(y = (-15).dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Área externa que forma a borda de seleção
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(
                    color = if (selecionado)
                        VerdeFundo
                    else
                        Branco
                )
                .border(
                    width = if (selecionado) 2.5.dp else 1.dp,
                    color = if (selecionado)
                        VerdePrincipal
                    else
                        BordaPadrao,
                    shape = CircleShape
                )
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            // Círculo verde principal
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = VerdePrincipal,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Ícone personalizado de resíduos
                Image(
                    painter = painterResource(
                        id = R.drawable.icone_residuos
                    ),
                    contentDescription = "Meus resíduos",
                    modifier = Modifier.size(27.dp),
                    colorFilter = ColorFilter.tint(Branco)
                )
            }
        }

        Text(
            text = "Resíduos",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (selecionado)
                VerdePrincipal
            else
                HomeTextoSecundario,
            maxLines = 1
        )
    }
}

// Barra inferior com as cinco telas principais
@Composable
fun BarraNavegacaoInferior(
    rotaAtual: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Branco
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 20.dp
                )
                .offset(y = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navega para Home
            ItemNavegacaoInferior(
                titulo = "Home",
                selecionado = rotaAtual == "home",
                iconeVetorial = Icons.Default.Home,
                onClick = {
                    onNavigate("home")
                },
                modifier = Modifier.weight(1f)
            )

            // Navega para Coletas
            ItemNavegacaoInferior(
                titulo = "Coletas",
                selecionado = rotaAtual == "coletas",
                iconeDrawable = R.drawable.icone_coletas,
                onClick = {
                    onNavigate("coletas")
                },
                modifier = Modifier.weight(1f)
            )

            // Botão central elevado: Meus Resíduos
            BotaoCentralNavegacao(
                selecionado = rotaAtual == "residuos",
                onClick = {
                    onNavigate("residuos")
                },
                modifier = Modifier.weight(1.2f)
            )

            // Navega para Histórico
            ItemNavegacaoInferior(
                titulo = "Histórico",
                selecionado = rotaAtual == "historico",
                iconeDrawable = R.drawable.icone_historico,
                onClick = {
                    onNavigate("historico")
                },
                modifier = Modifier.weight(1f)
            )

            // Navega para Perfil
            ItemNavegacaoInferior(
                titulo = "Perfil",
                selecionado = rotaAtual == "perfil",
                iconeDrawable = R.drawable.icone_perfil,
                onClick = {
                    onNavigate("perfil")
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}