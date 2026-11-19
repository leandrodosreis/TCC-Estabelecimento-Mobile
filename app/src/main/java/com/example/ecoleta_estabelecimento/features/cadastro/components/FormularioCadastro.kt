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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.ViewModel
import com.example.ecoleta_estabelecimento.features.cadastro.CadastroViewModel
import com.example.ecoleta_estabelecimento.features.cadastro.service.ViaCepClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun CampoDeCadastro(
    viewModel: CadastroViewModel,
    onSair: () -> Unit,
    modifier: Modifier = Modifier
) {

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


    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroCnpj by remember { mutableStateOf<String?>(null) }
    var erroCep by remember { mutableStateOf<String?>(null) }
    var erroLogradouro by remember { mutableStateOf<String?>(null) }
    var erronumero by remember { mutableStateOf<String?>(null) }
    var erroEmail by remember { mutableStateOf<String?>(null) }
    var erroTelefone by remember { mutableStateOf<String?>(null) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var erroConfirmarsenha by remember { mutableStateOf<String?>(null) }

    var mostrarErroCadastro by remember { mutableStateOf(false) }
    var mensagemErroCadastro by remember { mutableStateOf("") }







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
                    modifier = Modifier.padding(top = 20.dp, start = 30.dp, end = 10.dp)
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
                value = viewModel.nomeEmpresa,
                onValueChange = {
                    if (it.length <= limiteMaximoNome) {
                        viewModel.nomeEmpresa = it
                        erroNome = null
                    }
                },
                erro = erroNome
            )
        }

        // CNPJ e CEP
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "CNPJ",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = viewModel.cnpj,
                    onValueChange = {
                        viewModel.cnpj = it.uppercase()
                            .filter { c -> c in 'A'..'Z' || c in '0'..'9' }
                            .take(14)
                        erroCnpj = null
                    },
                    erro = erroCnpj,
                    keyboardType = KeyboardType.Number,
                    visualTransformation = MascaraCnpj()
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "CEP",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = viewModel.cep,
                    onValueChange = {
                        viewModel.onCepChange(it)
                        erroCep = null

                    },
                    erro = erroCep,
                    keyboardType = KeyboardType.Number,
                    visualTransformation = MascaraCep()
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
                value = viewModel.logradouro,
                onValueChange = {
                    if (it.length <= limiteMaximo) {
                       viewModel.logradouro = it
                        erroLogradouro = null
                    }
                },
                erro = erroLogradouro
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
                    value = viewModel.bairro,
                    onValueChange = { },
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
                    value = viewModel.cidade,
                    onValueChange = { },
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
                    value = viewModel.nomeEstado,
                    onValueChange = {},
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
                    value = viewModel.numero,
                    onValueChange = {
                        if (it.length <= limiteMaximo){
                            viewModel.numero = it
                            erronumero = null
                        }
                    },
                    keyboardType = KeyboardType.Number,
                    erro = erronumero
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
                    value = viewModel.email,
                    onValueChange = {
                        if (it.length <= limiteMaximoEmail) {
                            viewModel.email = it
                            erroEmail = null
                        }

                    },
                    keyboardType = KeyboardType.Email,
                    erro = erroEmail
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Telefone",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                CampoTexto(
                    value = viewModel.telefone,
                    onValueChange = {
                        viewModel.telefone = it.filter { c -> c.isDigit() }.take(11)
                        erroTelefone = null
                    },
                    keyboardType = KeyboardType.Phone,
                    visualTransformation = MascaraTelefone(),
                    erro = erroTelefone
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
                    value = viewModel.senha,
                    onValueChange = {
                        if (it.length <= limiteMaximo) {
                            viewModel.senha = it
                            erroSenha = null
                        }
                    },
                    erro = erroSenha,
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
                    value = viewModel.confirmarSenha,
                    onValueChange = {
                        if (it.length <= limiteMaximo) {
                            viewModel.confirmarSenha = it
                            erroConfirmarsenha = null
                        }
                    },
                    erro = erroConfirmarsenha,
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
            Button(
                onClick = onSair,
                modifier = Modifier
                    .weight(2f)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) { Text("Sair") }


            Button(
                onClick = {

                    erroNome = when {
                        viewModel.nomeEmpresa.isBlank() ->
                            "Informe o nome da empresa"

                        viewModel.nomeEmpresa.length < 3 ->
                            "O nome deve ter pelo menos 3 caracteres"

                        else -> null
                    }

                    erroCnpj = when {
                        viewModel.cnpj.isBlank() ->
                            "Informe o CNPJ"

                        viewModel.cnpj.length != 14 ->
                            "O CNPJ deve ter 14 números"

                        else -> null
                    }

                    erroCep = when {
                        viewModel.cep.isBlank() ->
                            "Informe o CEP"

                        viewModel.cep.length != 8 ->
                            "O CEP deve ter 8 números"

                        else -> null
                    }

                    erroLogradouro = if (viewModel.logradouro.isBlank()) {
                        "Informe o logradouro"
                    } else null

                    erronumero = if (viewModel.numero.isBlank()) {
                        "Informe o número"
                    } else null

                    erroEmail = when {
                        viewModel.email.isBlank() ->
                            "Informe o e-mail"

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(viewModel.email).matches() ->
                            "E-mail inválido"

                        else -> null
                    }

                    erroTelefone = when {
                        viewModel.telefone.isBlank() ->
                            "Informe o telefone"

                        viewModel.telefone.length !in 10..11 ->
                            "Telefone inválido"

                        else -> null
                    }

                    erroSenha = when {
                        viewModel.senha.isBlank() ->
                            "Informe a senha"

                        viewModel.senha.length < 8 ->
                            "A senha deve ter pelo menos 8 caracteres"

                        else -> null
                    }

                    erroConfirmarsenha = when {
                        viewModel.confirmarSenha.isBlank() ->
                            "Confirme sua senha"

                        viewModel.confirmarSenha != viewModel.senha ->
                            "As senhas não coincidem"

                        else -> null
                    }

                    val erros = listOf(
                        erroNome,
                        erroCnpj,
                        erroCep,
                        erroLogradouro,
                        erronumero,
                        erroEmail,
                        erroTelefone,
                        erroSenha,
                        erroConfirmarsenha
                    )

                    if (erros.any { it != null }) {
                        mensagemErroCadastro =
                            "Verifique os campos destacados e corrija as informações."
                        mostrarErroCadastro = true
                    } else {
                        viewModel.cadastrar()
                    }
                },

                modifier = Modifier
                    .weight(2f)
                    .height(50.dp),

                shape = RoundedCornerShape(15.dp),

                enabled = !viewModel.carregando,

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff52B788),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    if (viewModel.carregando) "Cadastrando..."
                    else "Cadastrar"
                )
            }


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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    erro: String? = null
) {
    val transformacao = if (senha) PasswordVisualTransformation() else visualTransformation

    val interactionSource = remember { MutableInteractionSource() }

    val fundo = if (readOnly) Color(0xffE9ECEF) else Color.White
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = fundo,
        unfocusedContainerColor = fundo
    )
    Column(modifier = modifier.fillMaxWidth()) {

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

        if (erro != null) {
            Text(
                text = erro,
                color = Color.Red,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp)
            )
        }

    }

}