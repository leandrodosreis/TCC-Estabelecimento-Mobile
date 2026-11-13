package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CampoDeCadastro(modifier: Modifier = Modifier) {

    var nomeEmpresa by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var logradouro by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val limiteMaximo = 100

    Column(
        modifier = modifier
            .background(Color(0xffF0F9F4))
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Nome da empresa
        Column {
            Text(
                text = "Nome da empresa",
                color = Color.Black,
                fontSize = 14.sp
            )
            CampoTexto(
                value = nomeEmpresa,
                onValueChange = { if (it.length <= limiteMaximo) nomeEmpresa = it }
            )
        }

        // CNPJ e CEP
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "CNPJ",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = cnpj,
                    onValueChange = { if (it.length <= limiteMaximo) cnpj = it },
                    keyboardType = KeyboardType.Number
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "CEP",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = cep,
                    onValueChange = { if (it.length <= limiteMaximo) cep = it },
                    keyboardType = KeyboardType.Number
                )
            }
        }

        // Logradouro
        Column {
            Text(
                text = "Logradouro",
                color = Color.Black,
                fontSize = 14.sp
            )
            CampoTexto(
                value = logradouro,
                onValueChange = { if (it.length <= limiteMaximo) logradouro = it }
            )
        }

        // Bairro e Cidade
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Bairro",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = bairro,
                    onValueChange = { if (it.length <= limiteMaximo) bairro = it }
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Cidade",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = cidade,
                    onValueChange = { if (it.length <= limiteMaximo) cidade = it }
                )
            }
        }

        // Estado e Número
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Estado",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = estado,
                    onValueChange = { if (it.length <= limiteMaximo) estado = it }
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Número do local",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = numero,
                    onValueChange = { if (it.length <= limiteMaximo) numero = it },
                    keyboardType = KeyboardType.Number
                )
            }
        }

        // Email e Telefone
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Email",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = email,
                    onValueChange = { if (it.length <= limiteMaximo) email = it },
                    keyboardType = KeyboardType.Email
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Telefone",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = telefone,
                    onValueChange = { if (it.length <= limiteMaximo) telefone = it },
                    keyboardType = KeyboardType.Phone
                )
            }
        }

        // Senha e Confirmar senha
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Senha",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = senha,
                    onValueChange = { if (it.length <= limiteMaximo) senha = it },
                    keyboardType = KeyboardType.Password,
                    senha = true
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Confirmar senha",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = confirmarSenha,
                    onValueChange = { if (it.length <= limiteMaximo) confirmarSenha = it },
                    keyboardType = KeyboardType.Password,
                    senha = true
                )
            }
        }

        // Botões
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button (
                onClick = { /* ação */ },
                modifier = Modifier.weight(2f).height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) { Text("Sair") }

            Button(
                onClick = { /* ação */ },
                modifier = Modifier.weight(2f).height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff52B788),
                    contentColor = Color.Black
                )
            ) { Text("Cadastrar") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    senha: Boolean = false,
    altura: Dp = 40.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(altura),
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = Color.Black),
        cursorBrush = SolidColor(Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            )
        }
    )
}