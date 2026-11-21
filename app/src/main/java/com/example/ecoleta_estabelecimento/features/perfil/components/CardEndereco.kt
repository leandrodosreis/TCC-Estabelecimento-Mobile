package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

// Card responsável pelos dados de endereço
@Composable
fun CardEndereco(
    modifier: Modifier = Modifier,
    cep: String,
    logradouro: String,
    numero: String,
    bairro: String,
    cidade: String,
    siglaEstado: String,
    // Controla o modo de edição
    editando: Boolean,
    // Callbacks dos campos editáveis
    onCepChange: (String) -> Unit,
    onLogradouroChange: (String) -> Unit,
    onNumeroChange: (String) -> Unit,
    consultandoCep: Boolean = false,
    erroCep: String? = null
) {
    // Junta logradouro e número somente para exibição
    val enderecoCompleto = listOf(
        logradouro,
        numero
    ).filter { it.isNotBlank() }
        .joinToString(", ")

    // Junta cidade e estado somente para exibição
    val cidadeEstado = listOf(
        cidade,
        siglaEstado
    ).filter { it.isNotBlank() }
        .joinToString(" / ")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título da seção
            Text(
                text = "ENDEREÇO",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textoPainel
            )

            if (!editando) {
                // ==================
                // Modo Visualização
                // ==================
                CampoPerfil(
                    titulo = "Logradouro",
                    valor = enderecoCompleto,
                    editando = false,
                    onValorChange = {}
                )

                CampoPerfil(
                    titulo = "Bairro",
                    valor = bairro,
                    editando = false,
                    onValorChange = {}
                )

                CampoPerfil(
                    titulo = "Cidade / Estado",
                    valor = cidadeEstado,
                    editando = false,
                    onValorChange = {}
                )

                CampoPerfil(
                    titulo = "CEP",
                    valor = cep,
                    editando = false,
                    onValorChange = {}
                )

            } else {

                // ============
                // Modo edição
                // ============
                // O CEP será consultado pelo PerfilViewModel
                CampoPerfil(
                    titulo = "CEP",
                    valor = cep,
                    editando = true,
                    tipoTeclado = KeyboardType.Number,
                    onValorChange = onCepChange,
                    erro = erroCep
                )

                // Indicador enquanto consulta o ViaCEP
                if (consultandoCep) {
                    CircularProgressIndicator(
                        color = VerdePrincipal,
                        strokeWidth = 2.dp
                    )
                }

                // Logradouro pode ser corrigido manualmente
                CampoPerfil(
                    titulo = "Logradouro",
                    valor = logradouro,
                    editando = true,
                    onValorChange = onLogradouroChange
                )

                // Número informado pelo estabelecimento
                CampoPerfil(
                    titulo = "Número",
                    valor = numero,
                    editando = true,
                    tipoTeclado = KeyboardType.Number,
                    onValorChange = onNumeroChange
                )

                // Bairro preenchido automaticamente
                CampoPerfil(
                    titulo = "Bairro",
                    valor = bairro,
                    editando = true,
                    podeEditar = false,
                    onValorChange = {}
                )

                // Cidade e estado preenchidos automaticamente
                CampoPerfil(
                    titulo = "Cidade / Estado",
                    valor = cidadeEstado,
                    editando = true,
                    podeEditar = false,
                    onValorChange = {}
                )
            }
        }
    }
}