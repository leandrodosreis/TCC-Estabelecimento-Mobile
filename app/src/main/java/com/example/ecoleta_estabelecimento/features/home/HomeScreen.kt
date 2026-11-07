package com.example.ecoleta_estabelecimento.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.features.home.components.CardBoasVindas
import com.example.ecoleta_estabelecimento.features.home.components.CardProximoPasso
import com.example.ecoleta_estabelecimento.features.home.components.CardResumo
import com.example.ecoleta_estabelecimento.features.home.components.SecaoOfertasRecebidas
import com.example.ecoleta_estabelecimento.features.home.model.HomeResumo
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardVerdeClaro
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.HomeScreen(
    resumo: HomeResumo?,
    ofertas: List<OfertaResumo>,
    onCadastrarResiduo: () -> Unit = {},
    onVerTodasOfertas: () -> Unit = {},
    onVerOferta: (Int) -> Unit = {}
) {

    // Títulos da Home
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
                text = "Início",
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                color = nomeTela
            )

            // Espaçamento entre Nome da Tela e subTitulo
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitulo
            Text(
                text = "Acompanhe o resumo das suas atividades.",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = subTitulo
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // Card grande de boas-vindas
    item {
        CardBoasVindas(
            nomeEmpresa = resumo?.nomeEmpresa ?: "Estabelecimento",
            quantidadeMateriais = resumo?.somaTotalQuantidade?.let { "$it kg" } ?: "-"
        )
    }

    // Primeira linha da grade 2x2
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Resíduos disponíveis
            CardResumo(
                // resumo? - acessa o objeto somente se ele não for null
                valor = resumo?.residuosDisponiveis?.toString()?: "-",
                descricao = "Resíduos disponíveis",
                icone = R.drawable.box_img,
                modifier = Modifier.weight(1f)
            )

            // Quantidade de materiais
            CardResumo(
                valor = resumo?.somaTotalQuantidade?.let { "$it kg" } ?: "-",
                descricao = "Em materiais",
                icone = R.drawable.materiais_img,
                fundo = HomeCardVerdeClaro,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // Segunda linha da grade 2x2
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ofertas recebidas
            CardResumo(
                valor = resumo?.ofertasRecebidas?.toString()?: "-",
                descricao = "Ofertas recebidas",
                icone = R.drawable.oferta_img,
                modifier = Modifier.weight(1f)
            )

            // Coletas agendadas
            CardResumo(
                valor = resumo?.coletasAgendadas?.toString()?: "-",
                descricao = "Coletas agendadas",
                icone = R.drawable.calendario_img,
                modifier = Modifier.weight(1f)
            )
        }
    }

    // Card grande de próximo passo
    item {
        CardProximoPasso(
            onCadastrarResiduo = onCadastrarResiduo
        )
    }

    // Seção de ofertas recebidas
    item {
        SecaoOfertasRecebidas(
            ofertas = ofertas,
            onVerTodas = onVerTodasOfertas,
            onVerOferta = onVerOferta
        )
    }
}
