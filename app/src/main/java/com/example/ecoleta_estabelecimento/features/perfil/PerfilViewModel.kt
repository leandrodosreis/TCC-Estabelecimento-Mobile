
package com.example.ecoleta_estabelecimento.features.perfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilEstabelecimento
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilAtualizar
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha
import com.example.ecoleta_estabelecimento.features.perfil.service.PerfilService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import kotlinx.coroutines.launch

class PerfilViewModel : ViewModel() {

    // Serviço responsável pelas requisições HTTP do perfil
    private val perfilService = RetrofitFactory.criarServico(
        PerfilService::class.java
    )

    // =====================================================
    // GET - BUSCAR PERFIL
    // =====================================================

    // Guarda os dados do estabelecimento autenticado
    var perfil by mutableStateOf<PerfilEstabelecimento?>(null)
        private set

    // Indica se o perfil está sendo carregado
    var carregandoPerfil by mutableStateOf(false)
        private set

    // Guarda possíveis erros da consulta
    var erroCarregamentoPerfil by mutableStateOf<String?>(null)
        private set

    // Busca os dados do perfil do estabelecimento
    fun carregarPerfil() {

        // Evita consultas simultâneas
        if (carregandoPerfil) return

        carregandoPerfil = true
        erroCarregamentoPerfil = null

        viewModelScope.launch {
            try {

                // Executa o GET do perfil
                val resposta = perfilService.buscarPerfil()

                if (resposta.isSuccessful) {

                    val corpo = resposta.body()

                    if (corpo?.status == true) {

                        // Obtém o primeiro perfil retornado pela API
                        perfil = corpo.response.perfil.firstOrNull()

                    } else {
                        erroCarregamentoPerfil =
                            "Não foi possível carregar os dados do perfil."
                    }

                } else {

                    erroCarregamentoPerfil = when (resposta.code()) {
                        400 -> "O ID do estabelecimento é inválido."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Perfil do estabelecimento não encontrado."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao carregar perfil. Código: ${resposta.code()}"
                    }
                }

            } catch (e: Exception) {

                erroCarregamentoPerfil =
                    "Não foi possível conectar ao servidor."

            } finally {

                carregandoPerfil = false
            }
        }
    }


    // =====================================================
    // PUT - ATUALIZAR DADOS DO PERFIL
    // =====================================================

    // Indica se os dados estão sendo atualizados
    var atualizandoPerfil by mutableStateOf(false)
        private set

    // Guarda possíveis erros da atualização
    var erroAtualizacaoPerfil by mutableStateOf<String?>(null)
        private set

    // Indica se a atualização foi realizada
    var perfilAtualizado by mutableStateOf(false)
        private set

    // Envia os dados atualizados do estabelecimento
    fun atualizarPerfil(dados: PerfilAtualizar) {

        // Evita atualizações simultâneas
        if (atualizandoPerfil) return

        atualizandoPerfil = true

        // Limpa os resultados anteriores
        erroAtualizacaoPerfil = null
        perfilAtualizado = false

        viewModelScope.launch {
            try {

                // Envia o PUT para o backend
                val resposta = perfilService.atualizarPerfil(dados)

                if (resposta.isSuccessful) {

                    val corpo = resposta.body()

                    if (corpo?.status == true) {

                        // Confirma que a atualização foi realizada
                        perfilAtualizado = true

                        // Busca novamente os dados atualizados
                        carregarPerfil()

                    } else {

                        erroAtualizacaoPerfil =
                            corpo?.message ?: "Não foi possível atualizar o perfil."
                    }

                } else {

                    erroAtualizacaoPerfil = when (resposta.code()) {
                        400 -> "Os dados informados são inválidos."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Perfil do estabelecimento não encontrado."
                        409 -> "Conflito nos dados ou endereço informado."
                        415 -> "Formato da requisição não aceito pelo servidor."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao atualizar perfil. Código: ${resposta.code()}"
                    }
                }

            } catch (e: Exception) {

                erroAtualizacaoPerfil =
                    "Não foi possível atualizar o perfil. Verifique sua conexão."

            } finally {

                atualizandoPerfil = false
            }
        }
    }

    // Limpa os resultados anteriores da atualização
    fun limparEstadoAtualizacaoPerfil() {
        erroAtualizacaoPerfil = null
        perfilAtualizado = false
    }


    // =====================================================
    // PATCH - ALTERAR SENHA
    // =====================================================

    // Indica se a senha está sendo alterada
    var alterandoSenha by mutableStateOf(false)
        private set

    // Guarda possíveis erros da alteração da senha
    var erroAlteracaoSenha by mutableStateOf<String?>(null)
        private set

    // Indica se a senha foi alterada com sucesso
    var senhaAlterada by mutableStateOf(false)
        private set

    // Envia os dados para alteração da senha
    fun alterarSenha(dados: PerfilSenha) {

        // Evita alterações simultâneas
        if (alterandoSenha) return

        alterandoSenha = true

        // Limpa os resultados anteriores
        erroAlteracaoSenha = null
        senhaAlterada = false

        viewModelScope.launch {
            try {

                // Envia o PATCH para o backend
                val resposta = perfilService.alterarSenha(dados)

                if (resposta.isSuccessful) {

                    val corpo = resposta.body()

                    if (corpo?.status == true) {

                        // Confirma que a senha foi alterada
                        senhaAlterada = true

                    } else {

                        erroAlteracaoSenha =
                            corpo?.message ?: "Não foi possível alterar a senha."
                    }

                } else {

                    erroAlteracaoSenha = when (resposta.code()) {
                        400 -> "Os dados informados são inválidos ou as senhas não coincidem."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Estabelecimento não encontrado."
                        409 -> "Senha atual incorreta ou nova senha igual à atual."
                        415 -> "Formato da requisição não aceito pelo servidor."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao alterar senha. Código: ${resposta.code()}"
                    }
                }

            } catch (e: Exception) {

                erroAlteracaoSenha =
                    "Não foi possível alterar a senha. Verifique sua conexão."

            } finally {

                alterandoSenha = false
            }
        }
    }

    // Limpa os resultados anteriores da alteração da senha
    fun limparEstadoAlteracaoSenha() {
        erroAlteracaoSenha = null
        senhaAlterada = false
    }


    // =====================================================
    // INICIALIZAÇÃO
    // =====================================================

    // Executado automaticamente quando a ViewModel é criada
    init {

        // Busca os dados do estabelecimento autenticado
        carregarPerfil()
    }
}
