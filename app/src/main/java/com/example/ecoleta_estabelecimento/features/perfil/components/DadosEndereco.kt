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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoBordaCampo
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoFundoCampo
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel



    @Composable
    fun CardEndereco(
        modifier: Modifier = Modifier,
        logradouro: String,
        bairro: String,
        cidade: String,
        estado: String,
        cep: String,
        numero: String
    ) {

        var logradouro by remember {
            mutableStateOf(logradouro)
        }

        var bairro by remember {
            mutableStateOf(bairro)
        }

        var cidade by remember {
            mutableStateOf(cidade)
        }

        var estado by remember {
            mutableStateOf(estado)
        }

        var cep by remember {
            mutableStateOf(cep)
        }

        var numero by remember {
            mutableStateOf(numero)
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
                    text = "ENDEREÇO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = textoPainel
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "CEP",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = cep,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = cep,
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
                    text = "Logradouro",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = logradouro,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = logradouro,
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
                    text = "Numero",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = numero,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = numero,
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

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = "Bairro",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = bairro,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = bairro,
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
                    text = "Cidade",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = cidade,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = cidade,
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
                    text = "Estado",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = estado,
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