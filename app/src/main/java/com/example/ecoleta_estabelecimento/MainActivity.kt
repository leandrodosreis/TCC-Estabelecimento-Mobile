package com.example.ecoleta_estabelecimento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecoleta_estabelecimento.components.footer.FooterScreen
import com.example.ecoleta_estabelecimento.components.header.HeaderScreen
import com.example.ecoleta_estabelecimento.components.layout.LayoutPadrao
import com.example.ecoleta_estabelecimento.features.home.HomeScreen
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.EcoletaEstabelecimentoTheme
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoletaEstabelecimentoTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),containerColor = Branco) { innerPadding ->
                   TelaInicial(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Composable responsável por controlar a tela exibida
@Composable
fun TelaInicial(
    modifier: Modifier = Modifier
) {

    // Controla temporariamente a rota selecionada
    var telaAtual by remember {
        mutableStateOf("home")
    }

    // Exibe o layout compartilhado das telas internas
    LayoutPadrao(
        modifier = modifier,
        rotaAtual = telaAtual,
        onNavigate = { rota ->
            telaAtual = rota
        },
        onLogout = {
            telaAtual = "login"
        }
    ) {

        // Exibe a tela correspondente à rota selecionada
        when (telaAtual) {

            "home" -> HomeScreen(
                ofertas = ofertasExemplo,

                onCadastrarResiduo = {
                    telaAtual = "residuos"
                },
                onVerTodasOfertas = {
                    telaAtual = "coletas"
                },
                onVerOferta = { idOferta ->
                    println("Oferta selecionada: $idOferta")
                }
            )

            // Telas temporárias para testar o menu
            "residuos" -> item {
                Text("Tela de Meus Resíduos")
            }

            "coletas" -> item {
                Text("Tela de Coletas")
            }

            "historico" -> item {
                Text("Tela de Histórico")
            }

            "perfil" -> item {
                Text("Tela de Perfil")
            }

            "login" -> item {
                Text("Tela de Login (teste)")
            }
        }
    }
}

// DADOS TEMPORÁRIOS PARA TESTAR OS CARDS DA HOME:
val ofertasExemplo = listOf(
    OfertaResumo(
        id = 1,
        nomeColetor = "XYZ Coletas",
        material = "Vidro",
        quantidadeKg = "15",
        data = "27/08/2026",
        valor = 150.00,
        status = "ACEITA"
    ),
    OfertaResumo(
        id = 2,
        nomeColetor = "XYZ Coletas",
        material = "Vidro",
        quantidadeKg = "15",
        data = "27/08/2026",
        valor = 150.00,
        status = "PENDENTE"
    )
)