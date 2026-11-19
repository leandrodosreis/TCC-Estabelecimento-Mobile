package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.*

@Composable
fun CardSenha(
    modifier: Modifier = Modifier,
    senhaInicial: String,
    onAlterarSenha: (String, String, String) -> Unit
) {

    var senha by remember(senhaInicial) {
        mutableStateOf(senhaInicial)
    }

    var mostrarDialogAlterarSenha by remember {
        mutableStateOf(false)
    }

    Column {

        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Branco
            ),
            border = BorderStroke(1.dp, HomeCardBorda)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "SEGURANÇA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = textoPainel
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Senha",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = senha,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Senha")
                    },
                    readOnly = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NovoResiduoFundoCampo,
                        unfocusedContainerColor = NovoResiduoFundoCampo,
                        focusedBorderColor = VerdePrincipal,
                        unfocusedBorderColor = NovoResiduoBordaCampo
                    )
                )

                Spacer(modifier = Modifier.height(1.dp))
            }

            BotaoAlterar(
                onClick = {
                    mostrarDialogAlterarSenha = true
                }
            )
        }

        if (mostrarDialogAlterarSenha) {

            DialogAlterarSenha(
                onDismiss = {
                    mostrarDialogAlterarSenha = false
                },
                onClick = { senhaAtual, novaSenha, confirmarSenha ->

                    onAlterarSenha(
                        senhaAtual,
                        novaSenha,
                        confirmarSenha
                    )

                    mostrarDialogAlterarSenha = false
                }
            )
        }
    }
}