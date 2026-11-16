package com.example.ecoleta_estabelecimento.features.cadastro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.cadastro.model.CadastrarUsuario
import com.example.ecoleta_estabelecimento.features.cadastro.model.UsuarioCadastrado
import com.example.ecoleta_estabelecimento.features.cadastro.model.UsuarioCadastroResponse
import com.example.ecoleta_estabelecimento.features.cadastro.service.CadastroService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
// AJUSTAR: importe o RetrofitClient que a ResiduosViewModel usa
// import com.example.ecoleta_estabelecimento.<pacote>.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CadastroViewModel : ViewModel() {

    // Campos do formulário
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

    // Estados da tela
    var carregando by mutableStateOf(false)
        private set
    var mensagemErro by mutableStateOf<String?>(null)
        private set
    var cadastroConcluido by mutableStateOf(false)
        private set

    private fun validar(): String? = when {
        nomeEmpresa.isBlank() -> "Informe o nome da empresa"
        cnpj.filter { it.isDigit() }.length != 14 -> "CNPJ deve ter 14 dígitos"
        cep.filter { it.isDigit() }.length != 8 -> "CEP deve ter 8 dígitos"
        logradouro.isBlank() || bairro.isBlank() || cidade.isBlank() -> "Preencha o endereço completo"
        estado.isBlank() || numero.isBlank() -> "Informe estado e número"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
        telefone.filter { it.isDigit() }.length < 10 -> "Telefone inválido"
        senha.length < 6 -> "A senha deve ter ao menos 6 caracteres"
        senha != confirmarSenha -> "As senhas não coincidem"
        else -> null
    }

    fun cadastrar() {
         if (carregando) return
        val erro = validar()
        if (erro != null){
            mensagemErro = erro
            return
        }

        viewModelScope.launch {
            carregando = true
            mensagemErro = null
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

                val servico = RetrofitFactory.criarServico(CadastroService::class.java)

                val resposta = servico.cadastrarEstabelecimento(request)

                val corpo = resposta.body()

                if (resposta.isSuccessful && corpo?.status == true) {
                    cadastroConcluido = true
                } else {
                    // Tenta ler a mensagem de erro enviada pelo backend
                    val corpoErro = resposta.errorBody()?.string()
                    val mensagem = try {
                        corpoErro?.let {
                            Gson().fromJson(
                                it,
                                UsuarioCadastroResponse::class.java
                            )?.message
                        }
                    } catch (e: Exception) {
                        null
                    }
                    mensagemErro = mensagem
                        ?: corpo?.message
                                ?: "Não foi possível cadastrar"
                }
            } catch (e: Exception) {
                mensagemErro = "Erro de conexão. Verifique sua internet."
            } finally {
                carregando = false
            }
        }
    }

    fun limparCadastroConcluido() {
        cadastroConcluido = false
    }
}