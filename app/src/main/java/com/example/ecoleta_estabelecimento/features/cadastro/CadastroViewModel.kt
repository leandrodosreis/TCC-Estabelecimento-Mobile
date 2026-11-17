
package com.example.ecoleta_estabelecimento.features.cadastro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.cadastro.model.CadastrarUsuario
import com.example.ecoleta_estabelecimento.features.cadastro.model.UsuarioCadastroResponse
import com.example.ecoleta_estabelecimento.features.cadastro.service.CadastroService
import com.example.ecoleta_estabelecimento.features.cadastro.service.ViaCepClient
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CadastroViewModel : ViewModel() {

    var nomeEmpresa by mutableStateOf("")
    var cnpj by mutableStateOf("")
    var cep by mutableStateOf("")
    var logradouro by mutableStateOf("")
    var bairro by mutableStateOf("")
    var cidade by mutableStateOf("")
    var estado by mutableStateOf("")
    var numero by mutableStateOf("")
    var email by mutableStateOf("")
    var telefone by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    var carregando by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf<String?>(null)
        private set

    var cadastroConcluido by mutableStateOf(false)
        private set

    var buscandoCep by mutableStateOf(false)
        private set

    private var jobCep: Job? = null

    fun onCepChange(novoCep: String) {
        cep = novoCep.filter { it.isDigit() }.take(8)

        jobCep?.cancel()
        limparEndereco()
        mensagemErro = null

        if (cep.length == 8) {
            buscarCep(cep)
        }
    }

    private fun buscarCep(cepDigitos: String) {
        jobCep = viewModelScope.launch {
            buscandoCep = true

            try {
                val resposta = ViaCepClient.service.buscarCep(cepDigitos)
                val dados = resposta.body()

                if (resposta.isSuccessful && dados != null && dados.erro != true) {
                    logradouro = dados.logradouro.orEmpty()
                    bairro = dados.bairro.orEmpty()
                    cidade = dados.cidade.orEmpty()
                    estado = dados.uf.orEmpty()
                } else {
                    limparEndereco()
                    mensagemErro = "CEP não encontrado. Verifique o número informado."
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                limparEndereco()
                mensagemErro = "Não foi possível consultar o CEP. Verifique sua conexão."
            } finally {
                buscandoCep = false
            }
        }
    }

    private fun limparEndereco() {
        logradouro = ""
        bairro = ""
        cidade = ""
        estado = ""
    }

    private fun validar(): String? = when {
        nomeEmpresa.isBlank() ->
            "Informe o nome da empresa"

        nomeEmpresa.trim().length < 3 ->
            "O nome da empresa deve ter pelo menos 3 caracteres"

        cnpj.filter { it.isDigit() }.length != 14 ->
            "CNPJ deve ter 14 dígitos"

        cep.filter { it.isDigit() }.length != 8 ->
            "CEP deve ter 8 dígitos"

        logradouro.isBlank() || bairro.isBlank() ||
                cidade.isBlank() || estado.isBlank() ->
            "Preencha o endereço completo com um CEP válido"

        numero.isBlank() ->
            "Informe o número do endereço"

        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
            "E-mail inválido"

        telefone.filter { it.isDigit() }.length < 10 ->
            "Telefone inválido"

        senha.length < 8 ->
            "A senha deve ter ao menos 8 caracteres"

        senha != confirmarSenha ->
            "As senhas não coincidem"

        else -> null
    }

    fun cadastrar() {
        if (carregando) return

        val erro = validar()

        if (erro != null) {
            mensagemErro = erro
            return
        }

        viewModelScope.launch {
            carregando = true
            mensagemErro = null
            cadastroConcluido = false

            try {
                val request = CadastrarUsuario(
                    nomeEmpresa = nomeEmpresa.trim(),
                    cnpj = cnpj.filter { it.isDigit() },
                    cep = cep.filter { it.isDigit() },
                    logradouro = logradouro.trim(),
                    bairro = bairro.trim(),
                    cidade = cidade.trim(),
                    estado = estado.trim(),
                    numero = numero.trim(),
                    email = email.trim(),
                    telefone = telefone.filter { it.isDigit() },
                    senha = senha
                )

                val servico = RetrofitFactory.criarServico(
                    CadastroService::class.java
                )

                val resposta = servico.cadastrarEstabelecimento(request)

                val corpo = resposta.body()

                if (resposta.isSuccessful && corpo?.status == true) {
                    cadastroConcluido = true
                } else {
                    val corpoErro = resposta.errorBody()?.string()

                    val mensagemServidor = try {
                        corpoErro?.let {
                            Gson().fromJson(
                                it,
                                UsuarioCadastroResponse::class.java
                            )?.message
                        }
                    } catch (e: Exception) {
                        null
                    }

                    mensagemErro = mensagemServidor
                        ?: corpo?.message
                                ?: "Não foi possível concluir o cadastro. Verifique os dados."
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensagemErro =
                    "Erro de conexão. Não foi possível acessar o servidor. Verifique sua internet."
            } finally {
                carregando = false
            }
        }
    }

    fun limparMensagemErro() {
        mensagemErro = null
    }

    fun limparCadastroConcluido() {
        cadastroConcluido = false
    }
}
