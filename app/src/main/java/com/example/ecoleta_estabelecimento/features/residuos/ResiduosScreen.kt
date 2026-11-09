package com.example.ecoleta_estabelecimento.features.residuos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.residuos.components.BotaoNovoResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.CardResiduo
import com.example.ecoleta_estabelecimento.features.residuos.components.FiltrosResiduos
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.ResiduosScreen(
    viewModel: ResiduosViewModel
) {

    // Títulos de Meus Resíduos
    item {
        Column {
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
            FiltrosResiduos()

            Spacer(modifier = Modifier.height(20.dp))

            // Chama o componente do Botão de adicionar novo resíduo
            BotaoNovoResiduo(
                onClick = {
                    // Futuramente abrirá a tela de cadastro de resíduos
                }
            )
        }
    }

    items(
        items = viewModel.residuosFiltrados,
        key = { residuo -> residuo.idResiduo }
    ) {
        residuo ->

        CardResiduo(
            residuo = residuo
        )
    }
}