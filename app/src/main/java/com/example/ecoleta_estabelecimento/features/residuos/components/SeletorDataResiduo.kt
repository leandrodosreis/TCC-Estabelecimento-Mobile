package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorDataResiduo(
    dataAtual: String,
    onConfirmar: (String) -> Unit,
    onCancelar: () -> Unit
) {
    // Obtém a data de hoje para abrir o calendário no mês atual
    val hoje = LocalDate.now()

    // Tenta recuperar a data que o usuário já digitou
    // Se estiver incompleta ou inválida, utiliza a data de hoje
    val dataInicial = remember(dataAtual) {
        try {
            LocalDate.parse(
                dataAtual,
                DateTimeFormatter.ofPattern("dd/MM/uuuu")
            )
        } catch (_: Exception) {
            hoje
        }
    }

    // O DatePicker trabalha com milissegundos em UTC
    fun converterParaMillis(data: LocalDate): Long {
        return data.atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    // Guarda a data selecionada no calendário
    val estadoCalendario = rememberDatePickerState(
        initialSelectedDateMillis = converterParaMillis(dataInicial),

        // Impede selecionar datas anteriores a hoje
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= converterParaMillis(hoje)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= hoje.year
            }
        }
    )

    // Exibe o calendário em uma janela
    DatePickerDialog(
        onDismissRequest = onCancelar,
        confirmButton = {
            TextButton(
                onClick = {
                    // Recupera a data selecionada pelo usuário
                    val millis = estadoCalendario.selectedDateMillis

                    if (millis != null) {
                        // Converte os milissegundos para uma data
                        val dataSelecionada = Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()

                        // Formata a data como DD/MM/AAAA.
                        val dataFormatada = dataSelecionada.format(
                            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                        )

                        // Envia a data escolhida para o formulário
                        onConfirmar(dataFormatada)
                    }
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    ) {
        // Calendário interativo do Material 3
        DatePicker(
            state = estadoCalendario
        )
    }
}