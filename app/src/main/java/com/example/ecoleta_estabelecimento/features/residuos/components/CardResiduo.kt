package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.CorTextoBase
import com.example.ecoleta_estabelecimento.ui.theme.Preto
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoFiltroTexto
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoIconeFundo
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoStatusAgendadoFundo
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoStatusAgendadoTexto
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoStatusAguardandoFinalizacaoFundo
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoStatusAguardandoFinalizacaoTexto
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoStatusDisponivelFundo
import com.example.ecoleta_estabelecimento.ui.theme.ResiduoStatusDisponivelTexto
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun CardResiduo(
    residuo: Residuo,
    onCancelar: (Int) -> Unit = {},
    onEditar: (Int) -> Unit = {},
    onVerColeta: (Int) -> Unit = {}
) {
    // Identifica a imagem de acordo com o material
    val imagemResiduo = when (residuo.material.trim().lowercase()) {
        "vidro" -> R.drawable.balde_vidro_sf
        "papel", "papelão" -> R.drawable.balde_papel_sf
        "plástico" -> R.drawable.balde_plastico_sf
        "metal" -> R.drawable.balde_metal_sf
        else -> R.drawable.balde_residuo_sf
    }

    // Verifica se o resíduo está agendado
    val agendado = residuo.statusCard.equals(
        "AGENDADO",
        ignoreCase = true
    )

    // Verifica se o backend informou que o resíduo está aguardando a finalização da coleta
    val aguardandoFinalizacao = residuo.statusCard.equals(
        "AGUARDANDO FINALIZAÇÃO",
        ignoreCase = true
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Branco
        ),
        border = BorderStroke(1.dp, BordaPadrao)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabeçalho do card
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Imagem do material
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            color = ResiduoIconeFundo,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(imagemResiduo),
                        contentDescription = "Imagem de ${residuo.material}",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Nome e descrição
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = residuo.material,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CorTextoBase
                    )

                    Text(
                        text = "Disponível para coleta",
                        fontSize = 12.sp,
                        color = subTitulo
                    )
                }

                // Status
                Text(
                    text = if (aguardandoFinalizacao)
                        "AGUARDANDO\nFINALIZAÇÃO"
                    else
                        residuo.statusCard,
                    fontSize = if (aguardandoFinalizacao) 11.sp else 13.sp,
                    lineHeight = if (aguardandoFinalizacao) 12.sp else 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (aguardandoFinalizacao)
                        ResiduoStatusAguardandoFinalizacaoTexto
                    else if (agendado)
                        ResiduoStatusAgendadoTexto
                    else
                        ResiduoStatusDisponivelTexto,
                    modifier = Modifier
                        .background(
                            color = if (aguardandoFinalizacao)
                                ResiduoStatusAguardandoFinalizacaoFundo
                            else if (agendado)
                                ResiduoStatusAgendadoFundo
                            else
                                ResiduoStatusDisponivelFundo,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(
                            horizontal = 9.dp,
                            vertical = 6.dp
                        )
                )
            }

            HorizontalDivider(color = BordaPadrao)

            // Informações do resíduo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Texto de quantidade
                    Text(
                        text = "Quantidade",
                        fontSize = 11.sp,
                        color = subTitulo
                    )

                    // Texto de kg
                    Text(
                        text = "${residuo.quantidade} kg",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CorTextoBase
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Texto de Data disponível
                    Text(
                        text = "Data disponível",
                        fontSize = 11.sp,
                        color = subTitulo
                    )

                    // Data disponível
                    Text(
                        text = residuo.dataDisponivel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = nomeTela
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Texto de Horário
                    Text(
                        text = "Horário",
                        fontSize = 11.sp,
                        color = subTitulo
                    )

                    // Texto para exibir o horário disponível para coleta
                    Text(
                        text = "${formatarHorario(residuo.horarioInicial)} às ${formatarHorario(residuo.horarioFinal)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = nomeTela
                    )
                }
            }

            HorizontalDivider(color = BordaPadrao)

            // Observação
            if (!residuo.observacao.isNullOrBlank()) {
                // Exibe o texto de observação, caso seja null, não exibe nada
                Text(
                    text = residuo.observacao,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = subTitulo
                )
            }

            // ===============
            // BOTÕES DE AÇÃO
            // ===============

            if (aguardandoFinalizacao) {
                // Esse status não tem interação de botão
                Text(
                    text = "Aguardando a empresa\ncoletora finalizar a coleta.",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = subTitulo,
                    textAlign = TextAlign.End,
                    lineHeight = 18.sp
                )

            } else if (agendado) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                OutlinedButton(
                    onClick = {
                        onVerColeta(residuo.idResiduo)
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(17.dp),
                    border = BorderStroke(1.dp, BordaPadrao),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Branco,
                        contentColor = ResiduoFiltroTexto
                    )
                ) {
                    Text(
                        text = "Ver coleta",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                    }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 30.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    OutlinedButton(
                        onClick = {
                            onCancelar(residuo.idResiduo)
                        },
                        modifier = Modifier
                            .width(130.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(17.dp),
                        border = BorderStroke(1.dp, BordaPadrao),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Branco,
                            contentColor = ResiduoFiltroTexto
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = {
                            onEditar(residuo.idResiduo)
                        },
                        modifier = Modifier
                            .width(130.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(17.dp),
                        border = BorderStroke(1.dp, BordaPadrao),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Branco,
                            contentColor = ResiduoFiltroTexto
                        )
                    ) {
                        Text(
                            text = "Editar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// Formata o horário para exibir apenas horas e minutos
// Exemplo: "08:00:00" -> "08:00"
private fun formatarHorario(horario: String): String {
    return horario.substringBeforeLast(
        delimiter = ":",
        missingDelimiterValue = horario
    ).let { resultado ->
        if (horario.count { it == ':' } >= 2) resultado else horario
    }
}