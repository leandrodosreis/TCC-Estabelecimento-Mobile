package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.cadastro.CadastroViewModel
import com.example.ecoleta_estabelecimento.features.login.components.MascaraCnpj
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.CadastroBotao
import com.example.ecoleta_estabelecimento.ui.theme.CadastroCampoReadOnly
import com.example.ecoleta_estabelecimento.ui.theme.CadastroCardFundo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.Preto
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun CampoDeCadastro(
    viewModel: CadastroViewModel,
    onSair: () -> Unit,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current

    val limiteMaximo = 100
    val limiteMaximoNome = 100
    val limiteMaximoEmail = 256

    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroCnpj by remember { mutableStateOf<String?>(null) }
    var erroCep by remember { mutableStateOf<String?>(null) }
    var erroLogradouro by remember { mutableStateOf<String?>(null) }
    var erronumero by remember { mutableStateOf<String?>(null) }
    var erroEmail by remember { mutableStateOf<String?>(null) }
    var erroTelefone by remember { mutableStateOf<String?>(null) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var erroConfirmarsenha by remember { mutableStateOf<String?>(null) }

    // Estados para controlar a visibilidade das senhas com o "olhinho"
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel.logradouro) {
        if (viewModel.logradouro.isNotBlank()) {
            erroLogradouro = null
        }
    }

    Column(
        modifier = modifier
            .background(VerdeFundo)
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 30.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .offset(y = (-10).dp),
                colors = CardDefaults.cardColors(
                    containerColor = CadastroCardFundo
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Text(
                    text = "CADASTRE-SE",
                    fontSize = 24.sp,
                    color = Preto,
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
                color = Preto,
                fontSize = 14.sp
            )

            CampoTexto(
                value = viewModel.nomeEmpresa,
                onValueChange = {
                    if (it.length <= limiteMaximoNome) {
                        viewModel.cadastrarNomeEmpresa(it)
                        erroNome = null
                    }
                },
                erro = erroNome ?: viewModel.erroNomeEmpresa
            )
        }

        // CNPJ e CEP
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    text = "CNPJ",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.cnpj,
                    onValueChange = { novoValor ->
                        val cnpjFiltrado = novoValor.uppercase()
                            .filter { c -> c in 'A'..'Z' || c in '0'..'9' }
                            .take(14)

                        viewModel.cadastrarCnpj(cnpjFiltrado)

                        erroCnpj = null
                    },
                    erro = erroCnpj ?: viewModel.erroCnpj,
                    keyboardType = KeyboardType.Ascii,
                    visualTransformation = MascaraCnpj()
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = "CEP",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.cep,
                    onValueChange = {
                        viewModel.onCepChange(
                            it.filter { c -> c.isDigit() }.take(8)
                        )

                        erroCep = null
                    },
                    erro = erroCep ?: viewModel.erroCep,
                    keyboardType = KeyboardType.Number,
                    visualTransformation = MascaraCep()
                )
            }
        }

        // Logradouro
        Column {
            Text(
                text = "Logradouro",
                color = Preto,
                fontSize = 14.sp
            )

            CampoTexto(
                value = viewModel.logradouro,
                onValueChange = {
                    if (it.length <= limiteMaximo) {
                        viewModel.cadastrarLogradouro(it)
                        // Limpa o erro do logradouro ao digitar ou atualizar
                        erroLogradouro = null
                    }
                },
                erro = erroLogradouro?: viewModel.erroLogradouro
            )
        }

        // Bairro e Cidade
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Bairro",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.bairro,
                    onValueChange = {},
                    readOnly = true
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Cidade",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.cidade,
                    onValueChange = {},
                    readOnly = true
                )
            }
        }

        // Estado e Número
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Estado",
                    color = Preto,
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
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.numero,
                    onValueChange = {
                        if (it.length <= limiteMaximo) {
                            viewModel.cadastrarNumero(it)
                            erronumero = null
                        }
                    },
                    keyboardType = KeyboardType.Number,
                    erro = erronumero?: viewModel.erroNumero
                )
            }
        }

        // Email e Telefone
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Email",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.email,
                    onValueChange = {
                        if (it.length <= limiteMaximoEmail) {
                            viewModel.cadastrarEmail(it)
                            erroEmail = null
                        }
                    },
                    keyboardType = KeyboardType.Email,
                    erro = erroEmail?: viewModel.erroEmail
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Telefone",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.telefone,
                    onValueChange = { novoValor ->
                        val telefoneFiltrado = novoValor
                            .filter { c -> c.isDigit() }.take(11)

                        viewModel.cadastrarTelefone(telefoneFiltrado)

                        erroTelefone = null
                    },
                    keyboardType = KeyboardType.Phone,
                    visualTransformation = MascaraTelefone(),
                    erro = erroTelefone?: viewModel.erroTelefone
                )
            }
        }

        // Senha e Confirmar senha (Com o olhinho de visibilidade)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Senha",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.senha,
                    onValueChange = {
                        if (it.length <= limiteMaximo) {
                            viewModel.cadastrarSenha(it)
                            erroSenha = null
                        }
                    },
                    erro = erroSenha?: viewModel.erroSenha,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(
                                imageVector = if (senhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                                tint = subTitulo
                            )
                        }
                    }
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Confirmar senha",
                    color = Preto,
                    fontSize = 14.sp
                )

                CampoTexto(
                    value = viewModel.confirmarSenha,
                    onValueChange = {
                        if (it.length <= limiteMaximo) {
                            viewModel.cadastrarConfirmarSenha(it)
                            erroConfirmarsenha = null
                        }
                    },
                    erro = erroConfirmarsenha?: viewModel.erroConfirmarSenha,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmarSenhaVisivel = !confirmarSenhaVisivel }) {
                            Icon(
                                imageVector = if (confirmarSenhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (confirmarSenhaVisivel) "Ocultar senha" else "Mostrar senha",
                                tint = subTitulo
                            )
                        }
                    }
                )
            }
        }

        if (viewModel.erroCadastro != null) {
            Text(
                text = viewModel.erroCadastro ?: "",
                color = NovoResiduoErro,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
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
                    containerColor = Branco,
                    contentColor = Preto
                )
            ) {
                Text("Sair")
            }

            Button(
                onClick = {

                    // Validação do nome
                    erroNome = when {
                        viewModel.nomeEmpresa.isBlank() ->
                            "Informe o nome da empresa"

                        viewModel.nomeEmpresa.length < 3 ->
                            "O nome deve ter pelo menos 3 caracteres"

                        else -> null
                    }

                    // Validação do CNPJ
                    erroCnpj = when {
                        viewModel.cnpj.isBlank() ->
                            "Informe o CNPJ"

                        viewModel.cnpj.length != 14 ->
                            "O CNPJ deve ter 14 caracteres"

                        else -> null
                    }

                    // Validação do CEP
                    erroCep = when {
                        viewModel.cep.isBlank() ->
                            "Informe o CEP"

                        viewModel.cep.length != 8 ->
                            "O CEP deve ter 8 números"

                        else -> null
                    }

                    // Validação do logradouro (Aqui ajustamos para limpar se já estiver preenchido)
                    erroLogradouro = if (viewModel.logradouro.isBlank()) {
                        "Informe o logradouro"
                    } else null

                    // Validação do número
                    erronumero = if (viewModel.numero.isBlank()) {
                        "Informe o número"
                    } else null

                    // Validação do email
                    erroEmail = when {
                        viewModel.email.isBlank() ->
                            "Informe o e-mail"

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(viewModel.email).matches() ->
                            "E-mail inválido"

                        else -> null
                    }

                    // Validação do telefone
                    erroTelefone = when {
                        viewModel.telefone.isBlank() ->
                            "Informe o telefone"

                        viewModel.telefone.length !in 10..11 ->
                            "Telefone inválido"

                        else -> null
                    }

                    // Validação da senha
                    erroSenha = when {
                        viewModel.senha.isBlank() ->
                            "Informe a senha"

                        viewModel.senha.length < 8 ->
                            "A senha deve ter pelo menos 8 caracteres"

                        else -> null
                    }

                    // Validação da confirmação de senha
                    erroConfirmarsenha = when {
                        viewModel.confirmarSenha.isBlank() ->
                            "Confirme sua senha"

                        viewModel.confirmarSenha != viewModel.senha ->
                            "As senhas não coincidem"

                        else -> null
                    }

                    // Verifica se existem erros
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

                    if (erros.none { it != null }) {
                        viewModel.cadastrar()
                    }

                },

                modifier = Modifier
                    .weight(2f)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                enabled = !viewModel.carregando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CadastroBotao,
                    contentColor = Preto
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
            return TransformedText(
                AnnotatedString(""),
                OffsetMapping.Identity
            )
        }

        val formatado = StringBuilder()
        var i = 0

        for (c in mascara) {
            if (c == '#') {
                if (i >= digitos.length) break
                formatado.append(digitos[i])
                i++
            } else {
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

                while (pos < formatado.length && mascara[pos] != '#') pos++

                return pos
            }

            override fun transformedToOriginal(offset: Int): Int {
                val ate = minOf(offset, formatado.length)
                return mascara.substring(0, ate).count { it == '#' }
            }
        }

        return TransformedText(
            AnnotatedString(formatado.toString()),
            mapeamento
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    altura: Dp = 40.dp,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    erro: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {

    val interactionSource = remember { MutableInteractionSource() }

    val fundo = if (readOnly) {
        CadastroCampoReadOnly
    } else {
        Branco
    }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = fundo,
        unfocusedContainerColor = fundo
    )

    Column(modifier = modifier.fillMaxWidth()) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .height(altura),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Preto
            ),
            cursorBrush = SolidColor(Preto),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->

                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 0.dp
                    ),
                    trailingIcon = trailingIcon,
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

        // Exibição do erro
        if (erro != null) {
            Text(
                text = erro,
                color = NovoResiduoErro,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp)
            )
        }
    }
}