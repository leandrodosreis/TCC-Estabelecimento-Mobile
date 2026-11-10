package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoCancelar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoTexto

@Composable
fun DialogNovoResiduo(
    onDismiss: () -> Unit
) {
    // Guarda temporariamente os valores digitados no formulário
    var tipoMaterial by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var dataDisponivel by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }


    // Dialog cria uma janela sobre a tela atual
    // O Android escurece automaticamente o fundo
    Dialog(
        onDismissRequest = onDismiss,

        // Permite fechar o popup ao tocar fora dele
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {

        // Obtém os controladores da própria janela do popup
        // Assim o foco será removido dos campos que pertencem ao Dialog
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        // Fundo branco e cantos arredondados do popup
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(15.dp),
            color = Branco
        ) {

            // Permite rolar o formulário quando não couber na tela
            Column(
                modifier = Modifier
                    .heightIn(max = 700.dp)
                    .verticalScroll(rememberScrollState())
                    // Detecta toques dentro do popup
                    .pointerInput(focusManager, keyboardController) {
                        awaitEachGesture {

                            // Detecta o toque antes de ele ser processado pelos componentes
                            awaitFirstDown(
                                requireUnconsumed = false,
                                pass = PointerEventPass.Initial
                            )

                            // Remove o foco do campo atualmente selecionado
                            focusManager.clearFocus(force = true)

                            // Esconde o teclado virtual
                            keyboardController?.hide()
                        }
                    }
                    .padding(
                        top = 30.dp,
                        bottom = 15.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Identificação do aplicativo
                Text(
                    text = "ECOLETA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovoResiduoConfirmar
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Título principal do formulário
                Text(
                    text = "Disponibilizar novo resíduo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = NovoResiduoTexto
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Descrição apresentada abaixo do título
                Text(
                    text = "Cadastre o material para receber ofertas de coleta. O valor será definido pela empresa coletora.",
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.widthIn(280.dp),
                    color = NovoResiduoDescricao
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para selecionar o tipo de material
                Text(
                    text = "Tipo de material",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = tipoMaterial,
                    onValueChange = { tipoMaterial = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        // Futuramente abrirá o dropdown de materiais
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Selecionar material"
                        )
                    },
                    colors = coresCamposNovoResiduo()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Campo numérico para informar a quantidade em quilogramas
                Text(
                    text = "Quantidade (kg)",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = coresCamposNovoResiduo()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Campo de data disponível
                Text(
                    text = "Data disponível",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = dataDisponivel,
                    onValueChange = { dataDisponivel = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = {
                        Text("DD/MM/AAAA")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    trailingIcon = {
                        // O calendário será conectado na próxima etapa
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Abrir calendário"
                        )
                    },
                    colors = coresCamposNovoResiduo()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Campo para informar o horário de disponibilidade
                Text(
                    text = "Horário",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = horario,
                    onValueChange = { horario = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    placeholder = {
                        Text("__:__ / __:__")
                    },
                    colors = coresCamposNovoResiduo()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Campo de observações adicionais
                Text(
                    text = "Observação",
                    fontSize = 12.sp,
                    color = NovoResiduoTexto
                )

                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    colors = coresCamposNovoResiduo()
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Botões inferiores do formulário
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancela o cadastro e fecha o popup
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoCancelar
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }

                    // Futuramente enviará o cadastro para o backend
                    Button(
                        onClick = {
                            // TODO: Integrar o POST de novo resíduo
                            // As validações de negócio serão feitas pelo backend
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoConfirmar
                        )
                    ) {
                        Text(
                            text = "Disponibilizar Resíduo",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// Centraliza a configuração visual dos campos do formulário
// Todos os campos compartilham as mesmas cores e bordas
@Composable
private fun coresCamposNovoResiduo() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = NovoResiduoFundoCampo,
        unfocusedContainerColor = NovoResiduoFundoCampo,
        focusedBorderColor = NovoResiduoBordaCampo,
        unfocusedBorderColor = NovoResiduoBordaCampo,
        focusedTextColor = NovoResiduoTexto,
        unfocusedTextColor = NovoResiduoTexto
    )
