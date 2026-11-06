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

    // Organiza o Header, conteúdo e Footer
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Branco)
    ) {

        // Header global fixo
        HeaderScreen (
            rotaAtual = rotaAtual,
            onNavigate = onNavigate,
            onLogout = onLogout
        )

        // Conteúdo rolável + Footer
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(VerdeFundo),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 40.dp,
                bottom = 0.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
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