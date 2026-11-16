package com.example.ecoleta_estabelecimento.features.coletas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoCancelar
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo

@Composable
fun DialogConfirmarAceiteOferta(
    onNao: () -> Unit,
    onSim: () -> Unit,
    carregando: Boolean = false,
    mensagemErro: String? = null
) {
    Dialog(
        onDismissRequest = {
            if (!carregando) {
                onNao()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Branco)
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "ECOLETA",
                color = VerdePrincipal,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Deseja realmente aceitar a oferta?",
                    color = VerdeEscuro,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 29.sp
                )

                Text(
                    text = "A ação não poderá ser desfeita",
                    color = subTitulo,
                    fontSize = 16.sp
                )
            }

            if (mensagemErro != null) {
                Text(
                    text = mensagemErro,
                    color = NovoResiduoErro,
                    fontSize = 13.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button (
                    onClick = onNao,
                    enabled = !carregando,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NovoResiduoCancelar,
                        contentColor = Branco
                    )
                ) {
                    Text(
                        text = "NÃO",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = onSim,
                    enabled = !carregando,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal,
                        contentColor = Branco
                    )
                ) {
                    if (carregando) {
                        CircularProgressIndicator(
                            color = Branco,
                            strokeWidth = 2.dp,
                            modifier = Modifier.height(22.dp)
                        )
                    } else {
                        Text(
                            text = "SIM",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}