package com.example.ecoleta_estabelecimento.features.perfil.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.HomeTextoSecundario
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

@Composable
fun CardDadosEmpresa(
    modifier: Modifier = Modifier,
    nomeEmpresa: String,
    cnpj: String,
    telefone: String,
    email: String
) {

    var nomeEmpresa by remember {
        mutableStateOf(nomeEmpresa)
    }

    var cnpj by remember {
        mutableStateOf(cnpj)
    }

    var telefone by remember {
        mutableStateOf(telefone)
    }

    var email by remember {
        mutableStateOf(email)
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
                text = "DADOS EMPRESA",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = textoPainel
            )

            Spacer(modifier = Modifier.height(5.dp))


            Text(
                text = "Nome da empresa",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            OutlinedTextField(
                value = nomeEmpresa,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                   Text(
                       text = nomeEmpresa,
                       fontSize = 24.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.Gray
                   )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NovoResiduoFundoCampo,
                    unfocusedContainerColor = NovoResiduoFundoCampo,
                    focusedBorderColor = VerdePrincipal,
                    unfocusedBorderColor = NovoResiduoBordaCampo
                )

            )
            Spacer( modifier = Modifier.height(1.dp))

            Text(
                text = "CNPJ",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            OutlinedTextField(
                value = cnpj,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = cnpj,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NovoResiduoFundoCampo,
                    unfocusedContainerColor = NovoResiduoFundoCampo,
                    focusedBorderColor = VerdePrincipal,
                    unfocusedBorderColor = NovoResiduoBordaCampo
                )

            )

            Text(
                text = "Telefone",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            OutlinedTextField(
                value = telefone,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp),
                placeholder = {
                    Text(
                        text = telefone,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NovoResiduoFundoCampo,
                    unfocusedContainerColor = NovoResiduoFundoCampo,
                    focusedBorderColor = VerdePrincipal,
                    unfocusedBorderColor = NovoResiduoBordaCampo
                )

            )

            Text(
                text = "E-mail",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )


            OutlinedTextField(
                value = email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp),
                placeholder = {
                    Text(
                        text = email,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NovoResiduoFundoCampo,
                    unfocusedContainerColor = NovoResiduoFundoCampo,
                    focusedBorderColor = VerdePrincipal,
                    unfocusedBorderColor = NovoResiduoBordaCampo
                )

            )
        }
    }
}