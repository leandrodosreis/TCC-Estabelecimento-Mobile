package com.example.ecoleta_estabelecimento.features.residuos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.features.residuos.ResiduosViewModel
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo
import com.example.ecoleta_estabelecimento.ui.theme.BordaPadrao
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoTexto
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import kotlinx.coroutines.delay

@Composable
fun DialogCancelarResiduo(
    residuo: Residuo,
    viewModel: ResiduosViewModel,
    onDismiss: () -> Unit,
    onConfirmar: (Int) -> Unit
) {

    // Controla o tempo de espera antes de permitir a remoção
    var segundosRestantes by remember(residuo.idResiduo) {
        mutableIntStateOf(3)
    }

    // Inicia a contagem regressiva ao abrir o popup
    LaunchedEffect(
        residuo.idResiduo
    ) {
        segundosRestantes = 3

        while (segundosRestantes > 0) {
            delay(1000)
            segundosRestantes--
        }
    }

    // Só permite confirmar quando a contagem terminar
    val podeConfirmar = segundosRestantes == 0 && !viewModel.cancelandoResiduo

    //Exibe o card
    Dialog(
        onDismissRequest = {
            if (!viewModel.cancelandoResiduo) {
                onDismiss()
            }
        },
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
                modifier = Modifier.padding(
                    top = 30.dp,
                    bottom = 25.dp,
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
                    color = VerdePrincipal
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Título do popup
                Text(
                    text = "Cancelar disponibilidade?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = NovoResiduoTexto
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Aviso sobre o cancelamento
                Text(
                    text = buildAnnotatedString {
                        append("Essa ação removerá a disponibilidade do ")

                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(residuo.material)
                        }
                        append(". O cancelamento não poderá ser desfeito. ")
                        append("Se quiser disponibilizar esse material novamente, ")
                        append("será necessário realizar uma nova disponibilidade.")
                    },
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = NovoResiduoDescricao
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Exibe os erros dentro do popup
                viewModel.erroCancelamentoResiduo?.let { mensagemErro ->
                    Text(
                        text = mensagemErro,
                        fontSize = 12.sp,
                        color = NovoResiduoErro
                    )
                }

                // Botões de ação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Fecha o popup sem cancelar o resíduo
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !viewModel.cancelandoResiduo,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            BordaPadrao
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Branco,
                            contentColor = NovoResiduoTexto
                        )
                    ) {
                        Text(
                            text = "Voltar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Confirma a remoção do resíduo
                    Button(
                        onClick = {
                            if (podeConfirmar) {
                                onConfirmar(residuo.idResiduo)
                            }
                        },
                        enabled = podeConfirmar,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePrincipal,
                            contentColor = Branco,

                            // Cor do botão enquanto estiver bloqueado
                            disabledContainerColor = BordaPadrao,
                            disabledContentColor = NovoResiduoTexto
                        )
                    ) {
                        if (viewModel.cancelandoResiduo) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = NovoResiduoTexto,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (segundosRestantes > 0) {
                                    "Aguarde ${segundosRestantes}s"
                                } else {
                                    "Confirmar remoção"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}