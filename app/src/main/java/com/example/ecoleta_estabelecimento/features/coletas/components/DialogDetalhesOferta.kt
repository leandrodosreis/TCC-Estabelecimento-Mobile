package com.example.ecoleta_estabelecimento.features.coletas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ecoleta_estabelecimento.features.coletas.ColetaExemplo
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoCancelar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaAceitaTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteFundo
import com.example.ecoleta_estabelecimento.ui.theme.OfertaPendenteTexto
import com.example.ecoleta_estabelecimento.ui.theme.OfertaTextoDetalhe
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
private fun CampoDetalheOferta(
    titulo: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = titulo,
            color = OfertaTextoDetalhe,
            fontSize = 12.sp
        )

        Text(
            text = valor,
            color = VerdeEscuro,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



@Composable
fun DialogDetalhesOferta(
    coleta: ColetaExemplo,
    onDismiss: () -> Unit,
    onAceitar: (ColetaExemplo, String) -> Unit,
    onRecusar: (ColetaExemplo) -> Unit
) {
    val pendente = coleta.status == "pendente"

    // Reinicia o campo quando outra oferta é selecionada
    var nomeFuncionario by remember(coleta.id) {
        mutableStateOf("")
    }

    var mostrarErro by remember(coleta.id) {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Branco)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ECOLETA",
                        color = VerdePrincipal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (pendente) "PENDENTE" else "ACEITA",
                        color = if (pendente) {
                            OfertaPendenteTexto
                        } else {
                            OfertaAceitaTexto
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                if (pendente) {
                                    OfertaPendenteFundo
                                } else {
                                    OfertaAceitaFundo
                                }
                            )
                            .padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            )
                    )
                }

                Text(
                    text = coleta.empresa,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeEscuro
                )
            }

            // PAINEL DE INFORMAÇÕES
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NovoResiduoFundoCampo)
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Material e data
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        CampoDetalheOferta(
                            titulo = "Material",
                            valor = coleta.material
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        CampoDetalheOferta(
                            titulo = "Data",
                            valor = "27/08/2026"
                        )
                    }
                }

                // Quantidade e horário
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        CampoDetalheOferta(
                            titulo = "Quantidade",
                            valor = coleta.quantidade
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        CampoDetalheOferta(
                            titulo = "Horário",
                            valor = "16:00 às 19:00"
                        )
                    }
                }

                HorizontalDivider(color = BordaPadrao)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Valor ofertado:",
                        fontSize = 13.sp,
                        color = OfertaTextoDetalhe
                    )

                    Text(
                        text = coleta.valor,
                        fontSize = 16.sp,
                        color = VerdeEscuro,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // OFERTA PENDENTE
            if (pendente) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Nome do funcionário *",
                        color = VerdeEscuro,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = nomeFuncionario,
                        onValueChange = {
                            nomeFuncionario = it

                            if (it.isNotBlank()) {
                                mostrarErro = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = mostrarErro,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                text = "Digite o nome do responsável",
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NovoResiduoFundoCampo,
                            unfocusedContainerColor = NovoResiduoFundoCampo,
                            focusedBorderColor = VerdePrincipal,
                            unfocusedBorderColor = NovoResiduoBordaCampo,
                            errorBorderColor = NovoResiduoErro
                        )
                    )

                    if (mostrarErro) {
                        Text(
                            text = "Informe o nome do funcionário para aceitar.",
                            color = NovoResiduoErro,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // Recusar não exige o nome do funcionário
                    Button(
                        onClick = { onRecusar(coleta) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoCancelar,
                            contentColor = Branco
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Recusar",
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }

                    // Aceitar exige o nome preenchido
                    Button(
                        onClick = {
                            val nome = nomeFuncionario.trim()

                            if (nome.isEmpty()) {
                                mostrarErro = true
                            } else {
                                onAceitar(coleta, nome)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePrincipal,
                            contentColor = Branco
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Aceitar",
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
            } else {
                // Oferta aceita: somente visualização
                Text(
                    text = "Esta oferta já foi aceita.",
                    fontSize = 13.sp,
                    color = OfertaTextoDetalhe
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Branco
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Entendi")
                }
            }
        }
    }
}