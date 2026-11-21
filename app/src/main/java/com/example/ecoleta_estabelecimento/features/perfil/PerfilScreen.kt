package com.example.ecoleta_estabelecimento.features.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoleta_estabelecimento.components.loading.CarregamentoEcoleta
import com.example.ecoleta_estabelecimento.features.perfil.components.CardDadosEmpresa
import com.example.ecoleta_estabelecimento.features.perfil.components.CardEndereco
import com.example.ecoleta_estabelecimento.features.perfil.components.CardResumos
import com.example.ecoleta_estabelecimento.features.perfil.components.CardSenha
import com.example.ecoleta_estabelecimento.features.residuos.components.MensagemListaVazia
import com.example.ecoleta_estabelecimento.ui.theme.Branco
import com.example.ecoleta_estabelecimento.ui.theme.HomeCardBorda
import com.example.ecoleta_estabelecimento.ui.theme.NovoResiduoErro
import com.example.ecoleta_estabelecimento.ui.theme.VerdeEscuro
import com.example.ecoleta_estabelecimento.ui.theme.VerdePrincipal
import com.example.ecoleta_estabelecimento.ui.theme.nomeTela
import com.example.ecoleta_estabelecimento.ui.theme.subTitulo
import com.example.ecoleta_estabelecimento.ui.theme.textoPainel

fun LazyListScope.PerfilScreen(
    viewModel: PerfilViewModel,
    onAlterarSenhaClick: () -> Unit
) {
    // Dados originais retornados pelo GET
    val perfil = viewModel.perfil

    // Valores exibidos nos campos
    val formulario = viewModel.formulario

    // Controla visualização ou edição
    val editando = viewModel.editando

    // CABEÇALHO DA TELA
    item {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // CARREGAMENTO INICIAL
    if (viewModel.carregandoPerfil && perfil == null) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                CarregamentoEcoleta.Circular()
            }
        }
        return
    }

    // ERRO OU PERFIL INDISPONÍVEL
    if (perfil == null) {
        item {
            MensagemListaVazia(
                titulo = "Não foi possível carregar o perfil",
                descricao = "Ocorreu um problema ao buscar os dados do perfil. Puxe a tela para baixo para atualizar."
            )
        }
        return
    }

    // RESUMO DO ESTABELECIMENTO
    item {
        CardResumos(
            // Mostra o nome atualmente exibido no formulário
            nomeEmpresa = formulario.nomeEmpresa,
            // O CNPJ vem do perfil oficial
            cnpjEmpresa = perfil.cnpj
        )
    }

    item {
        Spacer(modifier = Modifier.height(16.dp))
    }

    // CARD - DADOS DA EMPRESA
    item {
        CardDadosEmpresa(
            nomeEmpresa = formulario.nomeEmpresa,
            cnpj = perfil.cnpj,
            telefone = formulario.telefone,
            email = formulario.email,
            // Libera somente os campos autorizados
            editando = editando,
            // Atualizam os valores temporários armazenados na ViewModel
            onNomeChange = viewModel::atualizarNome,
            onTelefoneChange = viewModel::atualizarTelefone,
            onEmailChange = viewModel::atualizarEmail
        )
    }

    item {
        Spacer(modifier = Modifier.height(16.dp))
    }

    // CARD - ENDEREÇO
    item {
        CardEndereco(
            cep = formulario.cep,
            logradouro = formulario.logradouro,
            numero = formulario.numero,
            bairro = formulario.bairro,
            cidade = formulario.cidade,
            siglaEstado = formulario.siglaEstado,
            // Controla quais campos podem ser digitados
            editando = editando,
            // CEP: inicia consulta automática ao ViaCEP
            onCepChange = viewModel::atualizarCep,
            // Logradouro pode receber correções manuais
            onLogradouroChange = viewModel::atualizarLogradouro,
            // Número é digitado pelo usuário
            onNumeroChange = viewModel::atualizarNumero,
            // Estados da consulta ao ViaCEP
            consultandoCep = viewModel.consultandoCep,
            erroCep = viewModel.erroCep
        )
    }

    item {
        Spacer(modifier = Modifier.height(16.dp))
    }

    // CARD - SEGURANÇA
    item {
        CardSenha(
            // A senha nunca aparece no formulário
            // A alteração acontece em um popup separado
            onAlterarSenhaClick = onAlterarSenhaClick
        )
    }

    item {
        Spacer(modifier = Modifier.height(16.dp))
    }

    // MENSAGEM DE ERRO DO PUT
    if (viewModel.erroAtualizacaoPerfil != null) {
        item {
            Text(
                text = viewModel.erroAtualizacaoPerfil.orEmpty(),
                color = NovoResiduoErro,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
    }

    // MENSAGEM DE SUCESSO DO PUT
    if (viewModel.perfilAtualizado && !editando) {
        item {
            Text(
                text = "Perfil atualizado com sucesso!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = VerdePrincipal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
    }

    // BOTÕES DA TELA
    item {
        if (!editando) {
            // MODO VISUALIZAÇÃO

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.limparEstadoAtualizacaoPerfil()
                        viewModel.iniciarEdicao()
                    },
                    modifier = Modifier
                        .width(158.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(1.dp, HomeCardBorda),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Branco,
                        contentColor = VerdeEscuro
                    )
                ) {
                    Text(
                        text = "Editar perfil",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdeEscuro
                    )
                }
            }
        } else {
            // Botão Cancelar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.cancelarEdicao()
                    },
                    enabled = !viewModel.atualizandoPerfil,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HomeCardBorda),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Branco,
                        contentColor = VerdeEscuro
                    )
                ) {
                    Text(
                        text = "Cancelar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = VerdeEscuro
                    )
                }

                // Botão Salvar alterações
                OutlinedButton(
                    onClick = {
                        viewModel.salvarEdicao()
                    },
                    enabled = !viewModel.atualizandoPerfil &&
                            !viewModel.consultandoCep &&
                            viewModel.cepValidado,
                    modifier = Modifier
                        .weight(1.4f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HomeCardBorda),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Branco,
                        contentColor = VerdeEscuro
                    )
                ) {
                    if (viewModel.atualizandoPerfil) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = VerdePrincipal,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Salvar Alterações",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = VerdeEscuro,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }

    // Espaçamento ao final da lista
    item {
        Spacer(modifier = Modifier.height(24.dp))
    }
}