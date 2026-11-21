package com.example.ecoleta_estabelecimento.features.cadastro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.cadastro.model.CadastroEstabelecimentoRequest
import com.example.ecoleta_estabelecimento.features.cadastro.service.CadastroService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import com.example.ecoleta_estabelecimento.retrofit.viacep.ViaCepFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CadastroViewModel : ViewModel() {
    // Serviço responsável pelo cadastro do estabelecimento
    private val cadastroService = RetrofitFactory.criarServico(CadastroService::class.java)

    // Serviço responsável pela consulta de CEP
    private val viaCepService = ViaCepFactory.service

    // Controla uma consulta anterior ao ViaCEP
    private var consultaCepJob: Job? = null


    // =========================
    // DADOS DO CADASTRO
    // =========================

    var nomeEmpresa by mutableStateOf("")
        private set

    var cnpj by mutableStateOf("")
        private set

    var cep by mutableStateOf("")
        private set

    var logradouro by mutableStateOf("")
        private set

    var numero by mutableStateOf("")
        private set

    var bairro by mutableStateOf("")
        private set

    var cidade by mutableStateOf("")
        private set

    var nomeEstado by mutableStateOf("")
        private set

    var siglaEstado by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var telefone by mutableStateOf("")
        private set

    var senha by mutableStateOf("")
        private set

    var confirmarSenha by mutableStateOf("")
        private set


    // ===================
    // ESTADO DO CADASTRO
    // ===================

    // Indica se o cadastro está sendo enviado
    var carregando by mutableStateOf(false)
        private set

    // Guarda uma possível mensagem de erro da API
    var erroCadastro by mutableStateOf<String?>(null)
        private set

    // Indica que o cadastro foi concluído
    var cadastroRealizado by mutableStateOf(false)
        private set

    // Mensagem geral de validação do formulário
    var mensagemErroCadastro by mutableStateOf<String?>(null)
        private set


    // =================
    // ERROS DOS CAMPOS
    // =================

    var erroNomeEmpresa by mutableStateOf<String?>(null)
        private set

    var erroCnpj by mutableStateOf<String?>(null)
        private set

    var erroCep by mutableStateOf<String?>(null)
        private set

    var erroLogradouro by mutableStateOf<String?>(null)
        private set

    var erroNumero by mutableStateOf<String?>(null)
        private set

    var erroEmail by mutableStateOf<String?>(null)
        private set

    var erroTelefone by mutableStateOf<String?>(null)
        private set

    var erroSenha by mutableStateOf<String?>(null)
        private set

    var erroConfirmarSenha by mutableStateOf<String?>(null)
        private set


    // =================
    // ESTADO DO VIACEP
    // =================

    // Indica se o CEP está sendo consultado
    var consultandoCep by mutableStateOf(false)
        private set

    // Indica se o CEP foi consultado e validado
    var cepValidado by mutableStateOf(false)
        private set


    // ========================
    // ATUALIZAÇÃO DOS CAMPOS
    // ========================

    fun cadastrarNomeEmpresa(valor: String) {
        nomeEmpresa = valor.take(100)
        erroNomeEmpresa = null
    }

    fun cadastrarCnpj(valor: String) {
        cnpj = valor
            .uppercase()
            .filter { it in 'A'..'Z' || it in '0'..'9' }
            .take(14)

        erroCnpj = null
    }

    fun cadastrarLogradouro(valor: String) {
        logradouro = valor.take(100)
        erroLogradouro = null
    }

    fun cadastrarNumero(valor: String) {
        numero = valor
            .filter { it.isDigit() }

        erroNumero = null
    }

    fun cadastrarEmail(valor: String) {
        email = valor.take(256)
        erroEmail = null
    }

    fun cadastrarTelefone(valor: String) {
        // O backend aceita somente números
        telefone = valor
            .filter { it.isDigit() }
            .take(11)

        erroTelefone = null
    }

    fun cadastrarSenha(valor: String) {
        senha = valor.take(30)
        erroSenha = null
        erroConfirmarSenha = null
    }

    fun cadastrarConfirmarSenha(valor: String) {
        confirmarSenha = valor.take(30)
        erroConfirmarSenha = null
    }


    // ================
    // MAPA DE ESTADOS
    // ================

    // Converte a sigla recebida pelo ViaCEP no nome completo do estado

    private fun nomeEstadoPorSigla(
        sigla: String
    ): String {

        return mapOf(
            "AC" to "Acre",
            "AL" to "Alagoas",
            "AP" to "Amapá",
            "AM" to "Amazonas",
            "BA" to "Bahia",
            "CE" to "Ceará",
            "DF" to "Distrito Federal",
            "ES" to "Espírito Santo",
            "GO" to "Goiás",
            "MA" to "Maranhão",
            "MT" to "Mato Grosso",
            "MS" to "Mato Grosso do Sul",
            "MG" to "Minas Gerais",
            "PA" to "Pará",
            "PB" to "Paraíba",
            "PR" to "Paraná",
            "PE" to "Pernambuco",
            "PI" to "Piauí",
            "RJ" to "Rio de Janeiro",
            "RN" to "Rio Grande do Norte",
            "RS" to "Rio Grande do Sul",
            "RO" to "Rondônia",
            "RR" to "Roraima",
            "SC" to "Santa Catarina",
            "SP" to "São Paulo",
            "SE" to "Sergipe",
            "TO" to "Tocantins"
        )[sigla.uppercase()].orEmpty()
    }


    // ========
    // VIA CEP
    // ========

    fun onCepChange(valor: String) {

        val cepLimpo = valor
            .filter { it.isDigit() }
            .take(8)

        // Evita repetir a mesma consulta
        if (cep == cepLimpo) return

        // Cancela uma consulta anterior
        consultaCepJob?.cancel()
        consultaCepJob = null

        // Atualiza o CEP sem o hífen
        cep = cepLimpo

        // Limpa os estados anteriores
        consultandoCep = false
        erroCep = null
        cepValidado = false

        // Limpa o endereço anterior para não enviar
        // informações pertencentes a outro CEP
        logradouro = ""
        bairro = ""
        cidade = ""
        nomeEstado = ""
        siglaEstado = ""

        if (cepLimpo.length != 8) {
            return
        }

        // Inicia a consulta do novo CEP
        consultaCepJob = viewModelScope.launch {

            // Aguarda um pequeno intervalo antes da requisição
            delay(400)

            if (cep != cepLimpo) return@launch

            consultandoCep = true

            try {

                val resultado = viaCepService.consultarCep(cepLimpo)

                // Confirma se o CEP continua sendo o mesmo
                if (cep != cepLimpo) return@launch

                // CEP inexistente
                if (resultado.erro == true) {
                    erroCep = "CEP não encontrado."
                    return@launch
                }

                val cidadeEncontrada = resultado.localidade.orEmpty()

                val estadoEncontrado = resultado.uf
                    .orEmpty()
                    .uppercase()

                val nomeEstadoEncontrado = resultado.estado
                    ?.takeIf { it.isNotBlank() }
                    ?: nomeEstadoPorSigla(estadoEncontrado)

                // Verifica se os dados necessários foram encontrados
                if (
                    cidadeEncontrada.isBlank() ||
                    estadoEncontrado.length != 2 ||
                    nomeEstadoEncontrado.isBlank() ||
                    resultado.bairro.isNullOrBlank()
                ) {
                    erroCep = "Não foi possível obter o endereço completo para este CEP."
                    return@launch
                }

                // Preenche os campos automaticamente
                logradouro = resultado.logradouro.orEmpty()
                bairro = resultado.bairro
                cidade = cidadeEncontrada
                nomeEstado = nomeEstadoEncontrado
                siglaEstado = estadoEncontrado

                // Confirma que o CEP foi validado
                cepValidado = true
                erroCep = null

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                if (cep == cepLimpo) {
                    erroCep = "Não foi possível consultar o CEP. Verifique sua conexão."
                }

            } finally {
                if (cep == cepLimpo) {
                    consultandoCep = false
                }
            }
        }
    }

    // =============
    // LIMPAR ERROS
    // =============
    private fun limparErrosFormulario() {

        erroNomeEmpresa = null
        erroCnpj = null
        erroCep = null
        erroLogradouro = null
        erroNumero = null
        erroEmail = null
        erroTelefone = null
        erroSenha = null
        erroConfirmarSenha = null

        mensagemErroCadastro = null
        erroCadastro = null
    }

    // ========================
    // VALIDAÇÃO DO FORMULÁRIO
    // ========================
    private fun validarFormulario(): Boolean {
        limparErrosFormulario()

        erroNomeEmpresa = when {
            nomeEmpresa.isBlank() -> "Informe o nome da empresa"

            nomeEmpresa.trim().length < 3 -> "O nome deve ter pelo menos 3 caracteres"

            nomeEmpresa.length > 100 -> "O nome ultrapassou o limite permitido"

            else -> null
        }

        erroCnpj = when {
            cnpj.isBlank() -> "Informe o CNPJ"

            cnpj.length != 14 -> "O CNPJ deve possuir 14 caracteres"

            else -> null
        }

        erroCep = when {
            cep.isBlank() -> "Informe o CEP"

            cep.length != 8 -> "O CEP deve possuir 8 números"

            !cepValidado -> "Consulte um CEP válido antes de continuar"

            else -> null
        }

        erroLogradouro = when {
            logradouro.isBlank() -> "Informe o logradouro"

            logradouro.length > 100 -> "O logradouro ultrapassou o limite permitido"

            else -> null
        }

        erroNumero = when {
            numero.isBlank() -> "Informe o número"

            numero.toIntOrNull() == null -> "Informe um número válido"

            numero.toInt() < 1 -> "O número deve ser maior que zero"

            else -> null
        }

        erroEmail = when {
            email.isBlank() -> "Informe o e-mail"

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"

            else -> null
        }

        erroTelefone = when {
            telefone.isBlank() -> "Informe o telefone"

            telefone.length !in 10..11 -> "Telefone inválido"

            else -> null
        }

        erroSenha = when {
            senha.isBlank() -> "Informe a senha"

            senha.length < 8 -> "A senha deve ter pelo menos 8 caracteres"

            senha.length > 30 -> "A senha deve ter no máximo 30 caracteres"

            else -> null
        }

        erroConfirmarSenha = when {
            confirmarSenha.isBlank() -> "Confirme sua senha"

            confirmarSenha != senha -> "As senhas não coincidem"

            else -> null
        }

        val formularioValido = listOf(
            erroNomeEmpresa,
            erroCnpj,
            erroCep,
            erroLogradouro,
            erroNumero,
            erroEmail,
            erroTelefone,
            erroSenha,
            erroConfirmarSenha
        ).all { it == null } &&
                bairro.isNotBlank() &&
                cidade.isNotBlank() &&
                nomeEstado.isNotBlank() &&
                siglaEstado.length == 2

        if (!formularioValido) {
            mensagemErroCadastro = "Verifique os campos destacados e corrija as informações."
        }

        return formularioValido
    }


    // =================================
    // POST - CADASTRAR ESTABELECIMENTO
    // =================================

    fun cadastrar() {
        // Impede requisições simultâneas
        if (carregando) return

        // Aguarda a conclusão da consulta ao CEP
        if (consultandoCep) {
            erroCadastro = "Aguarde a consulta do CEP terminar."
            return
        }

        // Valida todos os campos
        if (!validarFormulario()) return

        carregando = true
        cadastroRealizado = false
        erroCadastro = null

        // Formata o CNPJ antes de enviar ao backend
        val cnpjFormatado = cnpj.replace(
            Regex("^(..)(...)(...)(....)(..)$"),
            "$1.$2.$3/$4-$5"
        )

        // Monta o objeto que será enviado ao backend
        val dadosCadastro = CadastroEstabelecimentoRequest(

            nomeEmpresa = nomeEmpresa.trim(),
            cnpj = cnpjFormatado,

            // Envia somente os 8 números, sem hífen
            cep = cep.filter { it.isDigit() },

            logradouro = logradouro.trim(),
            bairro = bairro.trim(),
            cidade = cidade.trim(),
            nomeEstado = nomeEstado.trim(),
            siglaEstado = siglaEstado,

            numeroLocal = numero.toInt(),

            email = email.trim(),
            telefone = telefone,

            senha = senha,
            confirmarSenha = confirmarSenha
        )

        // Executa a requisição dentro da coroutine da ViewModel
        viewModelScope.launch {
            try {
                // Envia o POST para o backend
                val resposta = cadastroService.cadastrarEstabelecimento(dadosCadastro)

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Cadastro confirmado pelo backend
                        cadastroRealizado = true

                    } else {
                        erroCadastro = corpo?.message?: "Não foi possível concluir o cadastro."
                    }
                } else {
                    // Trata os códigos HTTP retornados pela API
                    when (resposta.code()) {
                        400 -> {
                            val errorBodyString = resposta.errorBody()?.string().orEmpty()

                            // Direciona cada erro para o campo correspondente baseado no texto da API
                            if (errorBodyString.contains("CNPJ", ignoreCase = true)) {
                                erroCnpj = "O CNPJ informado é inválido."
                            }
                            if (errorBodyString.contains("e-mail", ignoreCase = true) || errorBodyString.contains("email", ignoreCase = true)) {
                                erroEmail = "E-mail inválido ou já cadastrado."
                            }
                            if (errorBodyString.contains("senha", ignoreCase = true)) {
                                erroSenha = "A senha não atende aos requisitos."
                            }

                            // Se nenhum campo específico foi preenchido, define o erro geral
                            if (erroCnpj == null && erroEmail == null && erroSenha == null) {
                                erroCadastro = "Não foi possível processar a requisição devido a erros de entrada de dados."
                            }
                        }
                        409 -> {
                            erroCadastro = "CNPJ, e-mail ou telefone já cadastrado."
                        }
                        415 -> {
                            erroCadastro = "Formato da requisição não aceito pelo servidor."
                        }
                        500 -> {
                            erroCadastro = "Ocorreu um erro interno no servidor."
                        }
                        else -> {
                            erroCadastro = "Erro ao realizar cadastro. Código: ${resposta.code()}"
                        }
                    }
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroCadastro = "Não foi possível conectar ao servidor. Verifique sua conexão."

            } finally {
                // Finaliza o carregamento em qualquer resultado
                carregando = false
            }
        }
    }

    // =========================
    // LIMPAR ESTADO DO CADASTRO
    // =========================
    fun limparEstadoCadastro() {

        consultaCepJob?.cancel()
        consultaCepJob = null

        carregando = false
        consultandoCep = false
        cadastroRealizado = false

        erroCadastro = null
        cepValidado = false

        limparErrosFormulario()
    }
}