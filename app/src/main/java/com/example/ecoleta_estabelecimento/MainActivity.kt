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

// Composable temporário para testar o Header, conteúdo e Footer
@Composable
fun TelaInicial(
    modifier: Modifier = Modifier
) {

    // Guarda a tela selecionada no menu
    var telaAtual by remember {
        mutableStateOf("home")
    }

    // Organiza os componentes da tela
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Branco)
    ) {

        // Exibe o Header
        HeaderScreen(
            rotaAtual = telaAtual,
            onNavigate = { rota ->
                telaAtual = rota
            },
            onLogout = {
                telaAtual = "login"
            }
        )

        // Conteúdo temporário da tela
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(VerdeFundo)
        ) {

            // Mostra a tela selecionada
            Text(
                text = "Tela atual: $telaAtual",
                modifier = Modifier.padding(24.dp)
            )
        }

        // Exibe o Footer
        FooterScreen()
    }
}