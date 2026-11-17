package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoConfirmar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoDescricao
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoTexto

@Composable
fun DialogAlterarSenha(
    onDismiss: () -> Unit
) {
    var SenhaAtual by remember {
        mutableStateOf( "")
    }

    var NovaSenha by remember {
        mutableStateOf( "")
    }

    var ConfirmarSenha by remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onDismiss,

        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(15.dp),
            color = Branco
        ) {

            Column(
                modifier = Modifier
                    .heightIn(max = 700.dp)
                    .verticalScroll(rememberScrollState())
                    // Detecta toques dentro do popup
                    .pointerInput(focusManager, keyboardController) {
                        awaitEachGesture {

                            // Detecta o toque antes de ele ser processado pelos componentes
                            awaitFirstDown(
                                requireUnconsumed = false,
                                pass = PointerEventPass.Initial
                            )

                            // Remove o foco do campo atualmente selecionado
                            focusManager.clearFocus(force = true)

                            // Esconde o teclado virtual
                            keyboardController?.hide()
                        }
                    }
                    .padding(
                        top = 30.dp,
                        bottom = 15.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Identificação do aplicativo
                Text(
                    text = "ECOLETA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovoResiduoConfirmar
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Título principal do formulário
                Text(
                    text = "ALterar senha",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = NovoResiduoTexto
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Descrição apresentada abaixo do título
                Text(
                    text = "Escolha uma senha forte para manter sua conta protegida.",
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.widthIn(280.dp),
                    color = NovoResiduoDescricao
                )

                Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
}