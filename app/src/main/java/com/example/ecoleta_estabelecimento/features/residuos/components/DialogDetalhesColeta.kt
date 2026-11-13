package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoTexto
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DialogDetalhesColeta(
    viewModel: ResiduosViewModel,
    onDismiss: () -> Unit
) {
    val detalhes = viewModel.detalhesColeta

    Dialog (
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(15.dp),
            color = Branco
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 700.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "ECOLETA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovoResiduoConfirmar
                )

                Text(
                    text = if (detalhes != null) {
                        "Detalhes da coleta - ${detalhes.material}"
                    } else {
                        "Detalhes da coleta"
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = CorTextoBase
                )

                Text(
                    text = "Confira as informações completas desta operação.",
                    fontSize = 13.sp,
                    color = NovoResiduoDescricao
                )

                // Controla carregamento, erro e sucesso
                when {
                    viewModel.carregandoDetalhesColeta -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(
                                Alignment.CenterHorizontally
                            ),
                            color = VerdePrincipal
                        )
                    }

                    viewModel.erroDetalhesColeta != null -> {
                        Text(
                            text = viewModel.erroDetalhesColeta?: "Não foi possível carregar a coleta.",
                            color = NovoResiduoErro,
                            fontSize = 13.sp
                        )
                    }

                    detalhes != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = VerdeFundo,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            LinhaDetalheColeta(
                                titulo = "Status:",
                                valor = detalhes.statusColeta
                            )

                            LinhaDetalheColeta(
                                titulo = "Empresa coletora:",
                                valor = detalhes.nomeEmpresaColetora
                            )

                            LinhaDetalheColeta(
                                titulo = "Ofertante:",
                                valor = detalhes.nomeOfertante
                            )

                            LinhaDetalheColeta(
                                titulo = "Aceito por:",
                                valor = detalhes.nomeAceitante
                            )

                            Text(
                                text = "Local da coleta:",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NovoResiduoDescricao,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            LinhaDetalheColeta(
                                titulo = "Endereço:",
                                valor = "${detalhes.logradouro}, ${detalhes.numero}"
                            )

                            LinhaDetalheColeta(
                                titulo = "Bairro:",
                                valor = detalhes.bairro
                            )

                            LinhaDetalheColeta(
                                titulo = "Cidade:",
                                valor = detalhes.cidade
                            )

                            LinhaDetalheColeta(
                                titulo = "Quantidade:",
                                valor = "${formatarQuantidadeColeta(detalhes.quantidade)} kg"
                            )

                            LinhaDetalheColeta(
                                titulo = "Data disponível:",
                                valor = detalhes.dataDisponivel
                            )

                            LinhaDetalheColeta(
                                titulo = "Horário:",
                                valor = "${detalhes.horarioInicial.take(5)} às " + detalhes.horarioFinal.take(5)
                            )

                            LinhaDetalheColeta(
                                titulo = "Observação:",
                                valor = detalhes.observacao?.ifBlank { "Não informada" }?: "Não informada"
                            )

                            LinhaDetalheColeta(
                                titulo = "Valor acordado:",
                                valor = formatarValorColeta( detalhes.valorAcordado)
                            )
                        }
                    }
                }

                Button (
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal
                    )
                ) {
                    Text(
                        text = "Entendi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Branco
                    )
                }
            }
        }
    }
}

// Formata a quantidade retornada pela API
@Composable
private fun LinhaDetalheColeta(
    titulo: String,
    valor: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 14.sp,
            color = NovoResiduoDescricao
        )

        Text(
            text = valor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeEscuro
        )
    }
}
private fun formatarQuantidadeColeta(quantidade: String): String {
    val numero = quantidade.replace(",", ".").toBigDecimalOrNull()
        ?: return quantidade

    return NumberFormat.getNumberInstance(
        Locale.forLanguageTag("pt-BR")
    ).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(numero)
}

// Formata o valor acordado em reais
private fun formatarValorColeta(valor: String): String {
    val numero = valor.replace(",", ".").toBigDecimalOrNull()
        ?: return valor

    return NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("pt-BR")
    ).format(numero)
}