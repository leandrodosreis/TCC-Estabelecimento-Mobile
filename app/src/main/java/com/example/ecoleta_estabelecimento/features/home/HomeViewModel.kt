package com.example.ecoleta_estabelecimento.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.home.model.HomeDados
import com.example.ecoleta_estabelecimento.features.home.service.HomeService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    // Cria o serviço responsável pela requisição HTTP da Home
    private val homeService = RetrofitFactory.criarServico(HomeService::class.java)

    // Dados recebidos da API
    var dadosHome by mutableStateOf<HomeDados?>(null) // O ? significa que os dados podem ser null
        private set // permite que a HomeScreen consulte o valor, mas impede que outras classes alterem o estado diretamente

    // Controla o carregamento
    var carregando by mutableStateOf(false)
        private set

    // Armazena mensagens de erro
    var mensagemErro by mutableStateOf<String?>(null)
        private set

    // Busca os dados assim que a ViewModel é criada
    init {
        buscarHome()
    }

    fun buscarHome() {
        // Utiliza a coroutine, onde o launch a inicia e permite chamar
        // a função suspend do HomeService sem bloquear a interface
        viewModelScope.launch {
            carregando = true
            mensagemErro = null

            try {
                val resposta = homeService.buscarHome()

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo != null && corpo.status) {
                        dadosHome = corpo.response
                    } else {
                        mensagemErro = "Não foi possível carregar os dados da Home."
                    }
                } else {
                    mensagemErro = when (resposta.code()) {
                        401 -> "Sessão inválida ou expirada. Verifique o token JWT."
                        404 -> "Estabelecimento não encontrado."
                        else -> "Erro ao carregar a Home: ${resposta.code()}"
                    }
                }

            } catch (e: Exception) {
                mensagemErro = "Erro de conexão com a API: ${e.message}"
            } finally {
                carregando = false
            }
        }
    }
}