import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoTexto
import com.example.ecoleta_estabelecimento.ui.theme.Preto
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal

@Composable
fun DialogAlterarSenha(
    onDismiss: () -> Unit,
    onClick: (PerfilSenha) -> Unit
) {
    var senhaAtual by remember {
        mutableStateOf("")
    }

    var novaSenha by remember {
        mutableStateOf("")
    }

    var confirmarSenha by remember {
        mutableStateOf("")
    }

    var mensagemErro by remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

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
                    .pointerInput(focusManager, keyboardController) {
                        awaitEachGesture {
                            awaitFirstDown(
                                requireUnconsumed = false,
                                pass = PointerEventPass.Initial
                            )

                            focusManager.clearFocus(force = true)
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

                Text(
                    text = "ECOLETA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovoResiduoConfirmar
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Alterar senha",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = NovoResiduoTexto
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Escolha uma senha forte para manter sua conta protegida.",
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.widthIn(280.dp),
                    color = NovoResiduoDescricao
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Senha atual
                Text(
                    text = "Senha atual",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = senhaAtual,
                    onValueChange = {
                        senhaAtual = senhaAtual
                        mensagemErro = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Digite sua senha atual")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NovoResiduoFundoCampo,
                        unfocusedContainerColor = NovoResiduoFundoCampo,
                        focusedBorderColor = VerdePrincipal,
                        unfocusedBorderColor = NovoResiduoBordaCampo
                    )
                )

                // Nova senha
                Text(
                    text = "Nova senha",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = novaSenha,
                    onValueChange = {
                        novaSenha = it
                        mensagemErro = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Digite sua nova senha")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NovoResiduoFundoCampo,
                        unfocusedContainerColor = NovoResiduoFundoCampo,
                        focusedBorderColor = VerdePrincipal,
                        unfocusedBorderColor = NovoResiduoBordaCampo
                    )
                )

                // Confirmar senha
                Text(
                    text = "Confirmar senha",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = confirmarSenha,
                    onValueChange = {
                        confirmarSenha = it
                        mensagemErro = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Confirme sua nova senha")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NovoResiduoFundoCampo,
                        unfocusedContainerColor = NovoResiduoFundoCampo,
                        focusedBorderColor = VerdePrincipal,
                        unfocusedBorderColor = NovoResiduoBordaCampo
                    )
                )

                // Mensagem de validação
                if (mensagemErro.isNotEmpty()) {
                    Text(
                        text = mensagemErro,
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            when {
                                senhaAtual.isBlank() ||
                                        novaSenha.isBlank() ||
                                        confirmarSenha.isBlank() -> {
                                    mensagemErro = "Preencha todos os campos."
                                }

                                novaSenha != confirmarSenha -> {
                                    mensagemErro = "As senhas não coincidem."
                                }

                                else -> {
                                    onClick(
                                        PerfilSenha(
                                            senhaAtual = senhaAtual,
                                            novaSenha = novaSenha,
                                            confirmarSenha = confirmarSenha
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .width(170.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(9.dp),
                        border = BorderStroke(1.dp, Preto),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Branco,
                            contentColor = Branco
                        )
                    ) {
                        Text(
                            text = "Salvar nova senha",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = Preto
                        )
                    }
                }
            }
        }
    }
}