package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

// Card com os dados cadastrais da empresa
@Composable
fun CardDadosEmpresa(
    modifier: Modifier = Modifier,
    nomeEmpresa: String,
    cnpj: String,
    telefone: String,
    email: String,
    // Controla o modo de visualização ou edição
    editando: Boolean,
    // Callbacks para atualizar os valores
    onNomeChange: (String) -> Unit,
    onTelefoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        border = BorderStroke(1.dp, HomeCardBorda)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Identificação da seção
            Text(
                text = "DADOS DA EMPRESA",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textoPainel
            )

            // Nome: editável quando solicitado
            CampoPerfil(
                titulo = "Nome da empresa",
                valor = nomeEmpresa,
                editando = editando,
                onValorChange = onNomeChange
            )

            // CNPJ: nunca editável
            CampoPerfil(
                titulo = "CNPJ",
                valor = cnpj,
                editando = editando,
                podeEditar = false,
                onValorChange = {}
            )

            // Telefone: editável
            CampoPerfil(
                titulo = "Telefone",
                valor = telefone,
                editando = editando,
                tipoTeclado = KeyboardType.Phone,
                onValorChange = onTelefoneChange
            )

            // E-mail: editável
            CampoPerfil(
                titulo = "E-mail",
                valor = email,
                editando = editando,
                tipoTeclado = KeyboardType.Email,
                onValorChange = onEmailChange
            )
        }
    }
}