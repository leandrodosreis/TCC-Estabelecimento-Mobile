package com.example.ecoleta_estabelecimento.features.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.auth.SessionManager
import com.example.ecoleta_estabelecimento.features.login.model.LoginRequest
import com.example.ecoleta_estabelecimento.features.login.service.LoginService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

// Controla a autenticação do estabelecimento
// AndroidViewModel permite acessar o Application, necessário para criar o SessionManager
class LoginViewModel (
    application: Application
) : AndroidViewModel(application) {

    // Serviço Retrofit responsável pelo POST de login
    private val loginService = RetrofitFactory.criarServico(LoginService::class.java)

    // Gerenciador responsável por criptografar, salvar e recuperar o JWT no dispositivo
    private val sessionManager = SessionManager(application)

    // Indica se uma tentativa de login está em andamento
    var carregandoLogin by mutableStateOf(false)
        private set

    // Mensagem de erro da tentativa de autenticação
    var erroLogin by mutableStateOf<String?>(null)
        private set

    // Indica que o backend confirmou o login e o JWT foi salvo com sucesso
    var loginRealizado by mutableStateOf(false)
        private set

    // Envia CNPJ e senha para o backend
    // O CNPJ já vem sem máscara do CampoCnpjLogin
    fun realizarLogin(cnpj: String, senha: String) {
        // Impede duas tentativas de login simultâneas
        if (carregandoLogin) return

        // Inicia o carregamento
        carregandoLogin = true

        // Limpa o resultado da tentativa anterior
        erroLogin = null
        loginRealizado = false

        // Inicia uma coroutine vinculada à ViewModel
        viewModelScope.launch {
            try {
                // Formata o CNPJ antes de enviar ao backend
                // O campo continua armazenando apenas os 14 caracteres
                val cnpjFormatado = cnpj.replace(
                    Regex("^(..)(...)(...)(....)(..)$"),
                    "$1.$2.$3/$4-$5"
                )

                // Monta o corpo JSON esperado pelo backend
                val dados = LoginRequest(
                    cnpj = cnpjFormatado,
                    senha = senha
                )

                // Envia o POST
                val resposta = loginService.realizarLogin(dados)

                if (resposta.isSuccessful) {
                    // Obtém o corpo da resposta
                    val corpo = resposta.body()

                    // O token fica dentro de response.token
                    val token = corpo?.response?.token

                    // Confirma o status da API e a existência de um JWT não vazio
                    if (corpo?.status == true && !token.isNullOrBlank()) {
                        // Criptografia e gravação no celular são executadas fora da thread principal
                        val salvo = withContext(Dispatchers.IO) {
                            sessionManager.salvarToken(token)
                        }

                        if (salvo) {
                            // A tela poderá navegar para a Home
                            loginRealizado = true
                        } else {
                            // Algum problema no Token
                            erroLogin = "Não foi possível salvar sua sessão. Tente novamente."
                        }
                    } else {
                        // HTTP de sucesso, mas o corpo não confirmou uma autenticação válida
                        erroLogin = "Não foi possível autenticar sua conta."
                    }
                } else {
                    // Trata os códigos HTTP
                    erroLogin = when (resposta.code()) {

                        // Dados da requisição inválidos
                        400 -> "Os dados informados são inválidos."
                        // CNPJ ou senha rejeitados pelo backend
                        401 -> "CNPJ ou senha estão incorretos."
                        // Content-Type não aceito
                        415 -> "Formato da requisição não aceito pelo servidor."
                        // Erro interno da API
                        500 -> "Ocorreu um erro interno no servidor."
                        // Qualquer outro código HTTP
                        else -> "Não foi possível realizar o login. Código: ${resposta.code()}"
                    }
                }
            } catch (e: IOException) {
                // Falhas de conexão, como servidor indisponível ou ausência de internet
                erroLogin = "Não foi possível conectar ao servidor. Verifique sua conexão."
            } catch (e: Exception) {
                // Trata falhas inesperadas sem expor informações internas do aplicativo
                erroLogin = "Ocorreu um erro inesperado ao realizar o login."
            } finally {
                // Finaliza o carregamento independentemente do resultado da tentativa
                carregandoLogin = false
            }
        }
    }

    // Limpa o resultado da autenticação
    // Será útil quando a tela precisar descartar a mensagem de erro ou o estado de sucesso
    // Não apaga o JWT salvo
    fun limparEstadoLogin() {
        erroLogin = null
        loginRealizado = false
    }
}