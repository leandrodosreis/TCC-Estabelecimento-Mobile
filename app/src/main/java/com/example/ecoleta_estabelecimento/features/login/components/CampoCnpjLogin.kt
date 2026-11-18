package com.example.ecoleta_estabelecimento.features.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.LoginBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun CampoCnpjLogin(
    cnpj: String,
    onCnpjChange: (String) -> Unit,
    enabled: Boolean = true,
    erro: String? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = cnpj,
            onValueChange = { novoValor ->
                val caracteres = novoValor
                    .uppercase()
                    .filter { it in 'A'..'Z' || it in '0'..'9' }
                    .take(14)

                onCnpjChange(caracteres)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            enabled = enabled,
            placeholder = {
                Text(
                    text = "CNPJ",
                    color = subTitulo
                )
            },
            singleLine = true,
            isError = erro != null,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = MascaraCnpj(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = VerdeEscuro
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePrincipal,
                unfocusedBorderColor = LoginBordaCampo,
                focusedContainerColor = Branco,
                unfocusedContainerColor = Branco,
                errorBorderColor = NovoResiduoErro,
                cursorColor = VerdePrincipal
            )
        )

        if (erro != null) {
            Text(
                text = erro,
                color = NovoResiduoErro,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    start = 1.dp,
                    top = 6.dp
                )
            )
        }
    }
}