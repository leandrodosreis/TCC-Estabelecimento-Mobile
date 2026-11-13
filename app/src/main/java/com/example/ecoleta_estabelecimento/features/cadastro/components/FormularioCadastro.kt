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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
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

    val limiteMaximoNome = 100
    val limiteMaximoCNPJ = 25
    val limiteMaximoCEP = 12
    val limiteMaximoEmail = 256
    val limiteMaximoTelefone = 25


    Column(
        modifier = modifier
            .background(Color(0xffF0F9F4))
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .offset(y = (-10).dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF9F6F6)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Text(
                    text = "CADASTRE-SE",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                Text(
                    text = "Disponibilize os resíduos para melhorar o meio ambiente, através da Ecoleta",
                    modifier = Modifier. padding(top = 20.dp, start = 30.dp, end = 10.dp)
                )

            }
        }

        // Nome da empresa
        Column {
            Text(
                text = "Nome da empresa",
                color = Color.Black,
                fontSize = 14.sp
            )
            CampoTexto(
                value = nomeEmpresa,
                onValueChange = { if (it.length <= limiteMaximoNome) nomeEmpresa = it }
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
                    onValueChange = { if (it.length <= limiteMaximoCNPJ) cnpj = it },
                    keyboardType = KeyboardType.Number,
                    visualTransformation = Mascara("##.###.###/####-##")
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
                    onValueChange = { if (it.length <= limiteMaximoCEP) cep = it },
                    keyboardType = KeyboardType.Number,
                    visualTransformation = Mascara("#####-###")
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
                    onValueChange = { if (it.length <= limiteMaximo) bairro = it },
                    readOnly = true
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
                    onValueChange = { if (it.length <= limiteMaximo) cidade = it },
                    readOnly = true
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
                    onValueChange = { if (it.length <= limiteMaximo) estado = it },
                    readOnly = true
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
                    onValueChange = { if (it.length <= limiteMaximoEmail) numero = it },
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
                    onValueChange = { if (it.length <= limiteMaximoTelefone) telefone = it },
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



class Mascara(private val mascara: String) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text

        if (digitos.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val formatado = StringBuilder()
        var i = 0

        for (c in mascara) {
            if (c == '#') {
                if (i >= digitos.length) break
                formatado.append(digitos[i])
                i++
            } else {
                // o separador aparece logo depois do último dígito digitado
                formatado.append(c)
            }
        }

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var vistos = 0
                var pos = 0
                while (pos < formatado.length && vistos < offset) {
                    if (mascara[pos] == '#') vistos++
                    pos++
                }
                // pula os separadores logo depois, para o cursor ficar após eles
                while (pos < formatado.length && mascara[pos] != '#') pos++
                return pos
            }

            override fun transformedToOriginal(offset: Int): Int {
                val ate = minOf(offset, formatado.length)
                return mascara.substring(0, ate).count { it == '#' }
            }
        }

        return TransformedText(AnnotatedString(formatado.toString()), mapeamento)
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
    altura: Dp = 40.dp,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val transformacao = if (senha) PasswordVisualTransformation() else visualTransformation

    val interactionSource = remember { MutableInteractionSource() }

    val fundo = if (readOnly) Color(0xffE9ECEF) else Color.White
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = fundo,
        unfocusedContainerColor = fundo
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
            .height(altura),
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = Color.Black),
        cursorBrush = SolidColor(Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = transformacao,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = transformacao,
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