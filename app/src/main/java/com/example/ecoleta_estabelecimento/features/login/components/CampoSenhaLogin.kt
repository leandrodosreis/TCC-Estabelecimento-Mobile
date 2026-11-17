package com.example.ecoleta_estabelecimento.features.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.LoginBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun CampoSenhaLogin(
    senha: String,
    onSenhaChange: (String) -> Unit,
    enabled: Boolean = true
) {
    var senhaVisivel by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = senha,
        onValueChange = onSenhaChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        enabled = enabled,
        placeholder = {
            Text(
                text = "Senha",
                color = subTitulo
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (senhaVisivel) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    senhaVisivel = !senhaVisivel
                }
            ) {
                Icon(
                    imageVector = if (senhaVisivel) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (senhaVisivel) {
                        "Ocultar senha"
                    } else {
                        "Mostrar senha"
                    },
                    tint = subTitulo
                )
            }
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = VerdeEscuro
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdePrincipal,
            unfocusedBorderColor = LoginBordaCampo,
            focusedContainerColor = Branco,
            unfocusedContainerColor = Branco,
            cursorColor = VerdePrincipal
        )
    )
}