package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

@Composable
fun CardSenha(
    modifier: Modifier = Modifier,
    senhaInicial: String

) {

    var senha by remember {
        mutableStateOf(senhaInicial)
    }
    var mostrarDialogAlterarSenha by remember {
        mutableStateOf(false)
    }

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
                onValueChange = { senha = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Senha")
                },
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

            Spacer( modifier = Modifier.height(1.dp))





        }
    }
    BotaoAlterar (
        onClick = {
            // Abre o popup de cadastro de novo resíduo
            mostrarDialogAlterarSenha = true
        }
    )
    if (mostrarDialogAlterarSenha) {
        DialogAlterarSenha (
            onDismiss = {
                // Fecha o popup ao clicar em Cancelar ou fora dele
                mostrarDialogAlterarSenha = false
            }
        )
    }
}


