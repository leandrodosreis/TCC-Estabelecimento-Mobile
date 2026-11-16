package com.example.ecoleta_estabelecimento.features.coletas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.coletas.components.CardColeta
import com.example.ecoleta_estabelecimento.features.coletas.components.FiltroColetas
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

// Será substituído pela model da API
data class ColetaExemplo(
    val id: Int,
    val empresa: String,
    val material: String,
    val quantidade: String,
    val distancia: String,
    val valor: String,
    val status: String
)

// Dados fictícios para construir o design
private val coletasExemplo = listOf(
    ColetaExemplo(
        id = 1,
        empresa = "XYZ Coletas",
        material = "Vidro",
        quantidade = "15 kg",
        distancia = "2,4 km",
        valor = "R$ 20,00",
        status = "pendente"
    ),
    ColetaExemplo(
        id = 2,
        empresa = "XYZ Coletas",
        material = "Vidro",
        quantidade = "15 kg",
        distancia = "2,4 km",
        valor = "R$ 20,00",
        status = "pendente"
    ),
    ColetaExemplo(
        id = 3,
        empresa = "XYZ Coletas",
        material = "Vidro",
        quantidade = "15 kg",
        distancia = "2,4 km",
        valor = "R$ 20,00",
        status = "aceita"
    )
)

fun LazyListScope.ColetasScreen(
    onVerMais: (ColetaExemplo) -> Unit = {}
) {
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
                text = "Coletas",
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

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    item {
        // Estado do filtro selecionado
        var filtroSelecionado by remember {
            mutableStateOf("Todas")
        }

        // Separação das ofertas por status
        val ofertasPendentes = coletasExemplo.filter {
            it.status == "pendente"
        }

        val ofertasAceitas = coletasExemplo.filter {
            it.status == "aceita"
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Filtro Todas / Pendentes / Aceitas
            FiltroColetas(
                filtroSelecionado = filtroSelecionado,
                onFiltroSelecionado = { novoFiltro ->
                    filtroSelecionado = novoFiltro
                }
            )

            // Ofertas pendentes
            if (filtroSelecionado == "Todas" || filtroSelecionado == "Pendentes") {
                SecaoColetas(
                    titulo = "Ofertas pendentes",
                    subtitulo = "ATENÇÃO NECESSÁRIA",
                    coletas = ofertasPendentes,
                    onVerMais = onVerMais
                )
            }

            // Ofertas aceitas
            if (filtroSelecionado == "Todas" || filtroSelecionado == "Aceitas") {
                SecaoColetas(
                    titulo = "Ofertas aceitas",
                    subtitulo = "OFERTAS CONFIRMADAS",
                    coletas = ofertasAceitas,
                    onVerMais = onVerMais
                )
            }
        }
    }
}

// Seção de ofertas
@Composable
private fun SecaoColetas(
    titulo: String,
    subtitulo: String,
    coletas: List<ColetaExemplo>,
    onVerMais: (ColetaExemplo) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = subtitulo,
            color = VerdePrincipal,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = titulo,
            color = nomeTela,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    }

    // Cards ou mensagem de lista vazia
    if (coletas.isEmpty()) {
        Text(
            text = "Nenhuma oferta encontrada.",
            color = HomeTextoSecundario,
            fontSize = 13.sp
        )
    } else {
        coletas.forEach { coleta ->
            CardColeta(
                coleta = coleta,
                onVerMais = {
                    onVerMais(coleta)
                }
            )
        }
    }
}