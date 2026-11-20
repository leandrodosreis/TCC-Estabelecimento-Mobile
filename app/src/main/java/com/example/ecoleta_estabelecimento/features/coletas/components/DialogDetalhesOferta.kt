package com.example.ecoleta_estabelecimento.features.coletas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ecoleta_estabelecimento.features.coletas.model.Coleta
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
import java.text.NumberFormat
import java.util.Locale

// Campo reutilizável de detalhes
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
            fontSize = 14.sp
        )

        Text(
            text = valor,
            color = VerdeEscuro,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// Formatação da quantidade
private fun formatarQuantidadeOferta(
    quantidade: String
): String {
    val texto = quantidade.trim()

    if (texto.endsWith("kg", ignoreCase = true)) {
        return texto
    }

    val numero = texto.replace(',', '.')
        .toBigDecimalOrNull()
        ?: return texto

    return "${numero.stripTrailingZeros().toPlainString().replace('.', ',')} kg"
}

// Formatação do valor
private fun formatarValorOferta(
    valor: String
): String {
    val texto = valor.trim()

    if (texto.startsWith("R$")) {
        return texto
    }

    val numero = texto.replace(',', '.')
        .toBigDecimalOrNull()
        ?: return texto

    return NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("pt-BR")
    ).format(numero)
}

// Formatação do horário
private fun formatarHorarioOferta(
    horario: String
): String {
    // Exemplo: 16:00:00 -> 16:00
    return horario.trim().take(5)
}

@Composable
fun DialogDetalhesOferta(
    coleta: Coleta,
    onDismiss: () -> Unit,
    onAceitar: (Coleta, String) -> Unit,
    onRecusar: (Coleta) -> Unit,
    carregandoOperacao: Boolean = false,
    mensagemErroOperacao: String? = null
) {
    // Status recebido da API
    val pendente = coleta.statusOferta
        .trim()
        .equals("pendente", ignoreCase = true)

    // Nome do funcionário informado no aceite
    var nomeFuncionario by remember(coleta.idOfertaInicial) {
        mutableStateOf("")
    }

    var mostrarErro by remember(coleta.idOfertaInicial) {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = {
            if (!carregandoOperacao) {
                onDismiss()
            }
        }
    ) {
        // O foco é controlado dentro da janela do Dialog
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        val limparFoco = {
            focusManager.clearFocus()
            keyboardController?.hide()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Branco)
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ) {
                    limparFoco()
                }
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            // Cabeçalho da oferta
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ECOLETA e botão de fechar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ECOLETA",
                        color = VerdePrincipal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            limparFoco()
                            onDismiss()
                        },
                        enabled = !carregandoOperacao
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar detalhes da oferta",
                            tint = VerdeEscuro
                        )
                    }
                }

                // Nome da empresa e status na mesma linha
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {

                    Text(
                        text = coleta.nomeEmpresaColetora,
                        modifier = Modifier.weight(1f),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeEscuro,
                        softWrap = true
                    )

                    Text(
                        text = if (pendente) {
                            "PENDENTE"
                        } else {
                            "ACEITA"
                        },
                        color = if (pendente) {
                            OfertaPendenteTexto
                        } else {
                            OfertaAceitaTexto
                        },
                        fontSize = 13.sp,
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
                            valor = coleta.dataDisponivel
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
                            valor = formatarQuantidadeOferta(coleta.quantidade)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        CampoDetalheOferta(
                            titulo = "Horário",
                            valor = "${
                                formatarHorarioOferta(coleta.horarioInicial)
                            } às ${
                                formatarHorarioOferta(coleta.horarioFinal)
                            }"
                        )
                    }
                }

                HorizontalDivider(color = BordaPadrao)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Valor ofertado:",
                        fontSize = 14.sp,
                        color = OfertaTextoDetalhe
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = formatarValorOferta(coleta.valorOfertado),
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
                        onValueChange = { novoTexto ->
                            // Permite letras e espaços, bloqueando números e símbolos
                            val nomeFiltrado = novoTexto.filter { caractere ->
                                caractere.isLetter() || caractere == ' '
                            }.take(100)

                            nomeFuncionario = nomeFiltrado

                            if (nomeFiltrado.isNotBlank()) {
                                mostrarErro = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !carregandoOperacao,
                        singleLine = true,
                        isError = mostrarErro,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                text = "Digite o nome do responsável",
                                fontSize = 13.sp
                            )
                        },
                        // Teclado configurado para nomes de pessoas
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        // Quando o usuário pressionar "Concluído"
                        keyboardActions = KeyboardActions(
                            onDone = {
                                limparFoco()
                            }
                        ),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NovoResiduoFundoCampo,
                            unfocusedContainerColor = NovoResiduoFundoCampo,
                            errorContainerColor = NovoResiduoFundoCampo,
                            focusedBorderColor = VerdePrincipal,
                            unfocusedBorderColor = NovoResiduoBordaCampo,
                            errorBorderColor = NovoResiduoErro,
                            focusedTextColor = VerdeEscuro,
                            unfocusedTextColor = VerdeEscuro,
                            errorTextColor = NovoResiduoErro,
                            errorCursorColor = NovoResiduoErro
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

                // Erro retornado pela viewModel
                if (mensagemErroOperacao != null) {
                    Text(
                        text = mensagemErroOperacao,
                        color = NovoResiduoErro,
                        fontSize = 12.sp
                    )
                }

                // Botões de aceitar e recusar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // Recusar não exige o nome do funcionário
                    Button(
                        onClick = {
                            limparFoco()
                            onRecusar(coleta)
                        },
                        enabled = !carregandoOperacao,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoCancelar,
                            contentColor = Branco
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Recusar",
                            fontSize = 17.sp,
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
                                limparFoco()
                                onAceitar(coleta, nome)
                            }
                        },
                        enabled = !carregandoOperacao,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePrincipal,
                            contentColor = Branco
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (carregandoOperacao) {
                            CircularProgressIndicator(
                                color = Branco,
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                            )

                        } else {
                            Text(
                                text = "Aceitar",
                                fontSize = 17.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            } else {
                // Oferta aceita: somente visualização
                Text(
                    text = "Esta oferta já foi aceita.",
                    fontSize = 15.sp,
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
                    Text(
                        text = "Entendi",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}