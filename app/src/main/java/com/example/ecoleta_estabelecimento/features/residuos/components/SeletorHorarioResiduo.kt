package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorHorarioResiduo(
    horarioAtual: String,
    onConfirmar: (String) -> Unit,
    onCancelar: () -> Unit
) {
    // Recupera o horário anterior, caso o usuário já tenha selecionado um.
    val partes = horarioAtual.split(":")

    val horaInicial = partes.getOrNull(0)?.toIntOrNull() ?: 0
    val minutoInicial = partes.getOrNull(1)?.toIntOrNull() ?: 0

    // Cria o seletor em formato de 24 horas.
    val estadoHorario = rememberTimePickerState(
        initialHour = horaInicial,
        initialMinute = minutoInicial,
        is24Hour = true
    )

    // Abre uma janela para selecionar o horário.
    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text("Selecionar horário")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Relógio interativo do Material 3.
                TimePicker(state = estadoHorario)
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    // Converte a seleção para HH:mm.
                    // Exemplo: 8 horas e 5 minutos -> 08:05.
                    val horarioFormatado = String.format(
                        Locale.ROOT,
                        "%02d:%02d",
                        estadoHorario.hour, estadoHorario.minute
                    )

                    onConfirmar(horarioFormatado)
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
    )
}