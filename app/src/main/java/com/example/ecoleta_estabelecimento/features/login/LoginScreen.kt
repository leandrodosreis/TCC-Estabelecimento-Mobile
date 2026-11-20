package com.example.ecoleta_estabelecimento.features.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.R
import com.example.ecoleta_estabelecimento.features.login.components.CampoCnpjLogin
import com.example.ecoleta_estabelecimento.features.login.components.CampoSenhaLogin
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeFundo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun LoginScreen(
    onEntrar: (String, String) -> Unit,
    onCadastro: () -> Unit,
    onEsqueceuSenha: () -> Unit,
    carregando: Boolean = false,
    erroAutenticacao: String? = null,
    onLimparErroAutenticacao: () -> Unit = {}
) {
    var cnpj by rememberSaveable {
        mutableStateOf("")
    }

    var senha by rememberSaveable {
        mutableStateOf("")
    }

    var erroCnpj by remember {
        mutableStateOf<String?>(null)
    }

    var erroSenha by remember {
        mutableStateOf<String?>(null)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeFundo)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Imagem superior com curva
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.floresta_login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Canvas(
                modifier = Modifier.matchParentSize()
            ) {
                val largura = size.width
                val altura = size.height

                val curva = Path().apply {
                    moveTo(0f, altura * 0.98f)

                    cubicTo(
                        largura * 0.24f,
                        altura * 0.52f,
                        largura * 0.76f,
                        altura * 0.92f,
                        largura,
                        altura * 0.48f
                    )

                    lineTo(largura, altura)
                    lineTo(0f, altura)
                    close()
                }

                drawPath(
                    path = curva,
                    color = VerdeFundo
                )
            }
        }

        // Conteúdo do Login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bem-vindo(a)!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeEscuro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Acesse sua conta no",
                fontSize = 17.sp,
                color = subTitulo,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Image(
                painter = painterResource(R.drawable.logo_ecoleta),
                contentDescription = "Ecoleta",
                modifier = Modifier
                    .width(225.dp)
                    .height(52.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Campo de CNPJ
            CampoCnpjLogin(
                cnpj = cnpj,
                onCnpjChange = {
                    cnpj = it
                    erroCnpj = null
                    onLimparErroAutenticacao()
                },
                enabled = !carregando,
                erro = erroCnpj,
                erroAutenticacao = erroAutenticacao != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            CampoSenhaLogin(
                senha = senha,
                onSenhaChange = {
                    senha = it
                    erroSenha = null
                    onLimparErroAutenticacao()
                },
                enabled = !carregando,
                erro = erroSenha ?: erroAutenticacao
            )

            // Esqueceu a senha
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(
                    onClick = onEsqueceuSenha
                ) {
                    Text(
                        text = "Esqueceu a senha?",
                        color = VerdePrincipal,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // BOTÃO DE ENTRAR
            Button(
                onClick = {
                    erroCnpj = when {
                        cnpj.isBlank() -> "Informe seu CNPJ."
                        cnpj.length != 14 -> "Informe um CNPJ com 14 caracteres."
                        else -> null
                    }

                    erroSenha = if (senha.isBlank()) {
                        "Informe sua senha."
                    } else {
                        null
                    }

                    if (erroCnpj == null && erroSenha == null) {
                        onEntrar(cnpj, senha)
                    }
                },
                enabled = !carregando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal
                )
            ) {
                if (carregando) {
                    CircularProgressIndicator(
                        color = Branco,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Entrar",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Branco
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Link para o cadastro
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Não possui uma conta?",
                    fontSize = 14.sp,
                    color = subTitulo
                )

                TextButton(
                    onClick = onCadastro,
                    contentPadding = PaddingValues(
                        horizontal = 6.dp
                    )
                ) {
                    Text(
                        text = "Cadastre-se",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdePrincipal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}