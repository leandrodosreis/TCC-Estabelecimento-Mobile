package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

// Campo reutilizável da tela de Perfil
@Composable
fun CampoPerfil(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    editando: Boolean,
    onValorChange: (String) -> Unit,
    podeEditar: Boolean = true,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    erro: String? = null
) {
    // Só libera digitação quando a tela estiver em edição E o campo permitir alteração
    val permitirEdicao = editando && podeEditar

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Nome exibido acima do campo
        Text(
            text = titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = subTitulo
        )

        OutlinedTextField(
            value = valor,
            // Não altera o dado se o campo estiver bloqueado
            onValueChange = { novoValor ->
                if (permitirEdicao) {
                    onValorChange(novoValor)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            // Mantém aparência normal, mas bloqueia a digitação
            readOnly = !permitirEdicao,
            singleLine = true,
            isError = erro != null,
            shape = RoundedCornerShape(8.dp),

            // Texto cinza na visualização, texto escuro durante a edição
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (permitirEdicao) {
                    VerdeEscuro
                } else {
                    subTitulo
                }
            ),
            keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NovoResiduoFundoCampo,
                unfocusedContainerColor = NovoResiduoFundoCampo,
                errorContainerColor = NovoResiduoFundoCampo,
                focusedBorderColor = VerdePrincipal,
                unfocusedBorderColor = NovoResiduoBordaCampo,
                errorBorderColor = NovoResiduoErro,
                cursorColor = VerdePrincipal
            )
        )

        // Exibe a mensagem de erro abaixo do campo
        if (erro != null) {
            Text(
                text = erro,
                fontSize = 12.sp,
                color = NovoResiduoErro
            )
        }
    }
}