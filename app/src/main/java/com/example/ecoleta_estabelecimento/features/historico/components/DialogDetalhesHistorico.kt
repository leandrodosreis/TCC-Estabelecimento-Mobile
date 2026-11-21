package com.example.ecoleta_estabelecimento.features.historico.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ecoleta_estabelecimento.components.loading.CarregamentoEcoleta
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

// Dados temporários
data class DetalhesHistorico(
    val tipo: TipoHistorico,
    val material: String,
    val quantidade: String,
    val data: String,
    val status: String,
    val nomeEmpresaColetora: String? = null,
    val nomeOfertante: String? = null,
    val nomeAceitante: String? = null,
    val logradouro: String? = null,
    val numero: String? = null,
    val bairro: String? = null,
    val cidade: String? = null,
    val horarioInicial: String? = null,
    val horarioFinal: String? = null,
    val observacao: String? = null,
    val valorAcordado: String? = null,
    val nomeColetor: String? = null,
    val placaVeiculo: String? = null,
    val dataColeta: String? = null,
    val horaColeta: String? = null
)


// Detalhes do histórico
@Composable
fun DialogDetalhesHistorico(
    detalhes: DetalhesHistorico?,
    onDismiss: () -> Unit,
    carregando: Boolean = false,
    mensagemErro: String? = null
) {
    Dialog (
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
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

                // Cabeçalho
                Text(
                    text = "ECOLETA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovoResiduoConfirmar
                )

                val titulo = when (detalhes?.tipo) {
                    TipoHistorico.CONCLUIDA -> "Detalhes da coleta - ${detalhes.material}"

                    TipoHistorico.CANCELADO -> "Detalhes do cancelamento - ${detalhes.material}"

                    null -> "Detalhes do histórico"
                }

                Text(
                    text = titulo,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = CorTextoBase
                )

                Text(
                    text = "Confira as informações completas desta operação.",
                    fontSize = 13.sp,
                    color = NovoResiduoDescricao
                )

                // Conteúdo do popup
                when {
                    carregando -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CarregamentoEcoleta.Circular()
                        }
                    }

                    mensagemErro != null -> {
                        Text(
                            text = mensagemErro,
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

                            LinhaDetalheHistorico(
                                titulo = "Status:",
                                valor = detalhes.status
                            )

                            // Coleta concluída
                            if (detalhes.tipo == TipoHistorico.CONCLUIDA) {

                                detalhes.nomeEmpresaColetora.exibirSeDisponivel("Empresa coletora:")

                                detalhes.nomeOfertante.exibirSeDisponivel("Ofertante:")

                                detalhes.nomeAceitante.exibirSeDisponivel("Aceito por:")

                                detalhes.nomeColetor.exibirSeDisponivel("Coletor:")

                                detalhes.placaVeiculo.exibirSeDisponivel("Placa do veículo:")
                            }

                            // Local da operação
                            val possuiEndereco =
                                !detalhes.logradouro.isNullOrBlank() ||
                                        !detalhes.bairro.isNullOrBlank() ||
                                        !detalhes.cidade.isNullOrBlank()

                            if (possuiEndereco) {
                                if (detalhes.tipo == TipoHistorico.CONCLUIDA) {
                                    Text(
                                        text = "Local da coleta:",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NovoResiduoDescricao,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                val endereco = listOfNotNull(
                                    detalhes.logradouro?.takeIf {
                                        it.isNotBlank()
                                    },
                                    detalhes.numero?.takeIf {
                                        it.isNotBlank()
                                    }
                                ).joinToString(", ")

                                endereco.exibirSeDisponivel("Endereço:")

                                detalhes.bairro.exibirSeDisponivel("Bairro:")

                                detalhes.cidade.exibirSeDisponivel("Cidade:")
                            }

                            // Informações do resíduo
                            if (detalhes.tipo == TipoHistorico.CANCELADO) {
                                LinhaDetalheHistorico(
                                    titulo = "Data disponível:",
                                    valor = detalhes.data
                                )

                                LinhaDetalheHistorico(
                                    titulo = "Quantidade:",
                                    valor = detalhes.quantidade
                                )

                            } else {
                                LinhaDetalheHistorico(
                                    titulo = "Quantidade:",
                                    valor = detalhes.quantidade
                                )

                                LinhaDetalheHistorico(
                                    titulo = "Data disponível:",
                                    valor = detalhes.data
                                )
                            }

                            val horario = listOfNotNull(
                                detalhes.horarioInicial
                                    ?.takeIf { it.isNotBlank() }
                                    ?.take(5),

                                detalhes.horarioFinal
                                    ?.takeIf { it.isNotBlank() }
                                    ?.take(5)
                            ).joinToString(" às ")

                            horario.exibirSeDisponivel("Horário:")

                            LinhaDetalheHistorico(
                                titulo = "Observação:",
                                valor = detalhes.observacao
                                    ?.ifBlank { "Não informada" }
                                    ?: "Não informada"
                            )

                            // Valor e finalização da coleta
                            if (detalhes.tipo == TipoHistorico.CONCLUIDA) {

                                detalhes.valorAcordado.exibirSeDisponivel("Valor acordado:")

                                detalhes.dataColeta.exibirSeDisponivel("Data da coleta:")

                                detalhes.horaColeta.exibirSeDisponivel("Hora da coleta:")
                            }
                        }
                    }
                }

                // Botão de fechar
                Button(
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

// Linha de informação
@Composable
private fun LinhaDetalheHistorico(
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

// Campo opcional
@Composable
private fun String?.exibirSeDisponivel(
    titulo: String
) {
    if (!this.isNullOrBlank()) {
        LinhaDetalheHistorico(
            titulo = titulo,
            valor = this
        )
    }
}