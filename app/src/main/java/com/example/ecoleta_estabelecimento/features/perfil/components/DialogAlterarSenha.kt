package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

// Campo de senha

@Composable
private fun CampoSenhaDialog(
    titulo: String,
    valor: String,
    onValorChange: (String) -> Unit,
    habilitado: Boolean,
    imeAction: ImeAction
) {
    // Cada campo controla sua própria visibilidade
    var senhaVisivel by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = titulo,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = subTitulo
        )

        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = habilitado,
            singleLine = true,
            shape = RoundedCornerShape(9.dp),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = VerdeEscuro
            ),
            visualTransformation = if (senhaVisivel) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),

            trailingIcon = {
                IconButton(
                    onClick = {
                        senhaVisivel = !senhaVisivel
                    },
                    enabled = habilitado
                ) {
                    Icon(
                        imageVector = if (senhaVisivel) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (senhaVisivel) {
                            "Ocultar $titulo"
                        } else {
                            "Mostrar $titulo"
                        },
                        tint = subTitulo
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NovoResiduoFundoCampo,
                unfocusedContainerColor = NovoResiduoFundoCampo,
                disabledContainerColor = NovoResiduoFundoCampo,
                focusedBorderColor = VerdePrincipal,
                unfocusedBorderColor = NovoResiduoBordaCampo,
                cursorColor = VerdePrincipal
            )
        )
    }
}

@Composable
fun DialogAlterarSenha(
    onDismiss: () -> Unit,
    onSalvar: (String, String, String) -> Unit,
    carregando: Boolean = false,
    mensagemErroApi: String? = null
) {
    var senhaAtual by rememberSaveable {
        mutableStateOf("")
    }

    var novaSenha by rememberSaveable {
        mutableStateOf("")
    }

    var confirmarSenha by rememberSaveable {
        mutableStateOf("")
    }

    var mensagemErroLocal by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Popup
    Dialog(
        onDismissRequest = {
            if (!carregando) {
                onDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !carregando,
            dismissOnClickOutside = !carregando
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Branco
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = 650.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 20.dp,
                        vertical = 28.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                // Cabeçalho
                Text(
                    text = "ECOLETA",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrincipal
                )

                Text(
                    text = "Alterar senha",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerdeEscuro
                )

                Text(
                    text = "Escolha uma senha forte para manter sua conta protegida",
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    color = subTitulo
                )

                // Campos de senha
                CampoSenhaDialog(
                    titulo = "Senha atual",
                    valor = senhaAtual,
                    onValorChange = {
                        senhaAtual = it
                        mensagemErroLocal = null
                    },
                    habilitado = !carregando,
                    imeAction = ImeAction.Next
                )

                CampoSenhaDialog(
                    titulo = "Nova senha",
                    valor = novaSenha,
                    onValorChange = {
                        novaSenha = it
                        mensagemErroLocal = null
                    },
                    habilitado = !carregando,
                    imeAction = ImeAction.Next
                )

                CampoSenhaDialog(
                    titulo = "Confirmar nova senha",
                    valor = confirmarSenha,
                    onValorChange = {
                        confirmarSenha = it
                        mensagemErroLocal = null
                    },
                    habilitado = !carregando,
                    imeAction = ImeAction.Done
                )

                // Mensagens de erro
                val erroExibido = mensagemErroLocal?: mensagemErroApi

                if (erroExibido != null) {
                    Text(
                        text = erroExibido,
                        fontSize = 13.sp,
                        color = NovoResiduoErro
                    )
                }

                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Botão vermelho para cancelar
                    Button(
                        onClick = onDismiss,
                        enabled = !carregando,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovoResiduoErro,
                            contentColor = Branco
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }

                    // Botão verde para salvar
                    Button(
                        onClick = {
                            mensagemErroLocal = when {
                                senhaAtual.isBlank() ||
                                        novaSenha.isBlank() ||
                                        confirmarSenha.isBlank() -> {
                                    "Preencha todos os campos"
                                }

                                novaSenha.length !in 8..30 -> {
                                    "A nova senha deve ter entre 8 e 30 caracteres"
                                }

                                novaSenha == senhaAtual -> {
                                    "A nova senha deve ser diferente da senha atual"
                                }

                                novaSenha != confirmarSenha -> {
                                    "As senhas não coincidem"
                                }
                                else -> null
                            }

                            if (mensagemErroLocal == null) {
                                onSalvar(
                                    senhaAtual,
                                    novaSenha,
                                    confirmarSenha
                                )
                            }
                        },
                        enabled = !carregando,
                        modifier = Modifier
                            .weight(1.6f)
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePrincipal,
                            contentColor = Branco
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        if (carregando) {
                            CircularProgressIndicator(
                                color = Branco,
                                strokeWidth = 2.dp,
                                modifier = Modifier.height(22.dp)
                            )
                        } else {
                            Text(
                                text = "Salvar nova senha",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}