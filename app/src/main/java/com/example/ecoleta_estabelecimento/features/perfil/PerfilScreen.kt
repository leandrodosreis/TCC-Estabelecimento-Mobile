
package com.example.ecoleta_estabelecimento.features.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.features.perfil.components.CardDadosEmpresa
import com.example.ecoleta_estabelecimento.features.perfil.components.CardEndereco
import com.example.ecoleta_estabelecimento.features.perfil.components.CardResumos
import com.example.ecoleta_estabelecimento.features.perfil.components.CardSenha
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.PerfilScreen(
    viewModel: PerfilViewModel
) {

    // Obtém os dados e estados da ViewModel
    val perfil = viewModel.perfil
    val carregando = viewModel.carregandoPerfil
    val erro = viewModel.erroCarregamentoPerfil

    // Títulos da tela
    item {
        Column {

            Text(
                text = "PAINEL DO ESTABELECIMENTO",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = textoPainel
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Meu perfil",
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                color = nomeTela
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Gerencie e acompanhe suas operações na Ecoleta.",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = subTitulo
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // Exibe carregamento enquanto busca os dados da API
    if (carregando && perfil == null) {

        item {
            CircularProgressIndicator()
        }

    }

    // Exibe mensagem caso ocorra algum erro
    if (erro != null && perfil == null) {

        item {
            Column {

                Text(
                    text = erro,
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color.Red
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.carregarPerfil()
                    }
                ) {
                    Text("Tentar novamente")
                }
            }
        }
    }

    // Exibe os cards quando os dados forem carregados
    if (perfil != null) {

        // Card de resumo do estabelecimento
        item {
            CardResumos(
                nomeEmpresa = perfil.nomeEmpresa,
                cnpjEmpresa = perfil.cnpj
            )
        }

        // Dados da empresa
        item {
            CardDadosEmpresa(
                nomeEmpresa = perfil.nomeEmpresa,
                cnpj = perfil.cnpj,
                telefone = perfil.telefone,
                email = perfil.email
            )
        }

        // Espaçamento entre os cards
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Endereço do estabelecimento
        item {
            CardEndereco(
                logradouro = perfil.logradouro,
                bairro = perfil.bairro,
                cidade = perfil.nomeCidade,
                estado = perfil.nomeEstado,
                cep = perfil.cep,
                numero = perfil.numero.toString()
            )
        }

        // Segurança
        item {
            CardSenha(
                senhaInicial = "********",
                onAlterarSenha = { senhaAtual, novaSenha, confirmarSenha ->

                    viewModel.alterarSenha(
                        PerfilSenha(
                            senhaAtual = senhaAtual,
                            novaSenha = novaSenha,
                            confirmarSenha = confirmarSenha
                        )
                    )
                }
            )
        }
    }
}
