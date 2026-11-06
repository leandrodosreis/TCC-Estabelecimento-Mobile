package com.example.ecoleta_estabelecimento.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.components.footer.FooterScreen
import com.example.ecoleta_estabelecimento.features.home.components.CardBoasVindas
import com.example.ecoleta_estabelecimento.features.home.components.CardProximoPasso
import com.example.ecoleta_estabelecimento.features.home.components.CardResumo
import com.example.ecoleta_estabelecimento.features.home.components.SecaoOfertasRecebidas
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardVerdeClaro
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.HomeScreen(
    onCadastrarResiduo: () -> Unit = {},
    ofertas: List<OfertaResumo>,
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
        CardBoasVindas()
    }

    // Primeira linha da grade 2x2
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Resíduos disponíveis
            CardResumo(
                valor = "05",
                descricao = "Resíduos disponíveis",
                icone = R.drawable.box_img,
                modifier = Modifier.weight(1f)
            )

            // Quantidade de materiais
            CardResumo(
                valor = "73 kg",
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
                valor = "02",
                descricao = "Ofertas recebidas",
                icone = R.drawable.oferta_img,
                modifier = Modifier.weight(1f)
            )

            // Coletas agendadas
            CardResumo(
                valor = "03",
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
