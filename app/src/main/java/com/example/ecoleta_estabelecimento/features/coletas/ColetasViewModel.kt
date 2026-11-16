package com.example.ecoleta_estabelecimento.features.coletas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.coletas.model.AceitarOfertaRequest
import com.example.ecoleta_estabelecimento.features.coletas.model.Coleta
import com.example.ecoleta_estabelecimento.features.coletas.service.ColetaService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ColetasViewModel : ViewModel() {
    private val coletaService = RetrofitFactory.criarServico(ColetaService::class.java)

    // Listagem das ofertas
    // Lista de ofertas recebidas
    var coletas by mutableStateOf<List<Coleta>>(emptyList())
        private set

    // Indica se a lista está sendo carregada
    var carregandoColetas by mutableStateOf(false)
        private set

    // Guarda possíveis erros da listagem
    var erroCarregamentoColetas by mutableStateOf<String?>(null)
        private set

    // Filtro selecionado na tela
    var filtroSelecionado by mutableStateOf("Todas")
        private set

    fun atualizarFiltro(filtro: String) {
        if (filtro in listOf("Todas", "Pendentes", "Aceitas")) {
            filtroSelecionado = filtro
        }
    }

    // Filtra as ofertas de acordo com a seleção
    val coletasFiltradas: List<Coleta>
        get() = coletas.filter { coleta ->
            when (filtroSelecionado) {

                "Pendentes" -> coleta.statusOferta
                    .trim()
                    .equals("pendente", ignoreCase = true)

                "Aceitas" -> coleta.statusOferta
                    .trim()
                    .equals("aceita", ignoreCase = true)

                else -> true
            }
        }

    // Ofertas pendentes
    val ofertasPendentes: List<Coleta>
        get() = coletas.filter {
            it.statusOferta.trim()
                .equals("pendente", ignoreCase = true)
        }

    // Ofertas aceitas
    val ofertasAceitas: List<Coleta>
        get() = coletas.filter {
            it.statusOferta.trim()
                .equals("aceita", ignoreCase = true)
        }

    // Busca todas as ofertas do estabelecimento
    fun carregarColetas() {
        // Evita consultas simultâneas da lista
        if (carregandoColetas) return

        carregandoColetas = true
        erroCarregamentoColetas = null

        viewModelScope.launch {
            try {
                val resposta = coletaService.listarColetas()

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {

                        // Atualiza a lista observada pelo Compose
                        coletas = corpo.response.resumo

                    } else {
                        erroCarregamentoColetas = "Não foi possível carregar as ofertas."
                    }
                } else {
                    erroCarregamentoColetas = when (resposta.code()) {
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Nenhuma oferta foi encontrada."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao carregar ofertas. Código: ${resposta.code()}"
                    }
                }
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroCarregamentoColetas =
                    "Não foi possível conectar ao servidor. Verifique sua conexão."

            } finally {
                carregandoColetas = false
            }
        }
    }

    // Detalhes de uma oferta
    // Oferta selecionada para exibir no diálogo
    var detalhesOferta by mutableStateOf<Coleta?>(null)
        private set

    // Indica se os detalhes estão sendo carregados
    var carregandoDetalhesOferta by mutableStateOf(false)
        private set

    // Guarda possíveis erros da consulta
    var erroDetalhesOferta by mutableStateOf<String?>(null)
        private set

    // Identifica a consulta mais recente
    private var versaoConsultaDetalhes = 0

    // Limpa os dados ao fechar ou abrir outra oferta
    fun limparDetalhesOferta() {
        versaoConsultaDetalhes++
        detalhesOferta = null
        erroDetalhesOferta = null
        carregandoDetalhesOferta = false
    }

    // Busca os detalhes pelo ID da oferta inicial
    fun buscarDetalhesColeta(idOfertaInicial: Int) {
        // Invalida resultados de consultas anteriores
        limparDetalhesOferta()

        val consultaAtual = versaoConsultaDetalhes

        carregandoDetalhesOferta = true

        viewModelScope.launch {
            try {
                val resposta = coletaService.buscarDetalhesColeta(idOfertaInicial)

                // Ignora resposta de um diálogo já fechado ou de outra oferta selecionada
                if (consultaAtual != versaoConsultaDetalhes) return@launch

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        detalhesOferta = corpo.response.resumo
                            .firstOrNull {
                                it.idOfertaInicial == idOfertaInicial
                            }

                        if (detalhesOferta == null) {
                            erroDetalhesOferta = "Os detalhes desta oferta não foram encontrados."
                        }
                    } else {
                        erroDetalhesOferta = "Não foi possível carregar os detalhes da oferta."
                    }
                } else {
                    erroDetalhesOferta = when (resposta.code()) {
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "A oferta não foi encontrada."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao consultar oferta. Código: ${resposta.code()}"
                    }
                }
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                if (consultaAtual == versaoConsultaDetalhes) {
                    erroDetalhesOferta = "Não foi possível conectar ao servidor."
                }
            } finally {
                if (consultaAtual == versaoConsultaDetalhes) {
                    carregandoDetalhesOferta = false
                }
            }
        }
    }

    // Aceitar uma oferta
    // Indica se o aceite está sendo enviado
    var aceitandoOferta by mutableStateOf(false)
        private set

    // Guarda erros do aceite
    var erroAceitarOferta by mutableStateOf<String?>(null)
        private set

    // ID da última oferta aceita com sucesso
    var idOfertaAceita by mutableStateOf<Int?>(null)
        private set

    // Limpa estados de tentativas anteriores
    fun limparEstadoAceitarOferta() {
        erroAceitarOferta = null
        idOfertaAceita = null
    }

    // Envia o aceite com o nome do funcionário
    fun aceitarOferta(
        idOferta: Int,
        nomeFuncionario: String
    ) {
        // Impede duas operações de oferta simultâneas
        if (aceitandoOferta || recusandoOferta) return

        val nome = nomeFuncionario.trim()

        // Nome obrigatório, entre 1 e 100 caracteres
        if (nome.isEmpty() || nome.length > 100) {
            erroAceitarOferta = "Informe um nome de funcionário válido, com até 100 caracteres."
            return
        }

        aceitandoOferta = true
        limparEstadoAceitarOferta()

        viewModelScope.launch {
            try {
                val resposta = coletaService.aceitarOferta(
                    idOferta = idOferta,
                    dados = AceitarOfertaRequest(nomeAceitante = nome)
                )

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Atualiza o status local imediatamente
                        coletas = coletas.map { coleta ->
                            if (coleta.idOfertaInicial == idOferta) {
                                coleta.copy(statusOferta = corpo.response.statusOferta)
                            } else {
                                coleta
                            }
                        }

                        // Informa à interface que deu certo
                        idOfertaAceita = idOferta

                        // Sincroniza os cards com o backend
                        carregarColetas()
                    } else {
                        erroAceitarOferta = corpo?.message ?: "Não foi possível aceitar a oferta."
                    }
                } else {
                    erroAceitarOferta = when (resposta.code()) {
                        400 -> "Os dados informados são inválidos."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "A oferta não foi encontrada."
                        409 -> "Esta oferta não pode ser aceita no estado atual."
                        415 -> "Formato da requisição não aceito pelo servidor."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao aceitar oferta. Código: ${resposta.code()}"
                    }
                }
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroAceitarOferta = "Não foi possível concluir o aceite. Verifique sua conexão."

            } finally {
                aceitandoOferta = false
            }
        }
    }

    // Recusar uma oferta
    // Indica se a recusa está sendo enviada
    var recusandoOferta by mutableStateOf(false)
        private set

    // Guarda erros da recusa
    var erroRecusarOferta by mutableStateOf<String?>(null)
        private set

    // ID da última oferta recusada com sucesso
    var idOfertaRecusada by mutableStateOf<Int?>(null)
        private set

    // Limpa estados de tentativas anteriores
    fun limparEstadoRecusarOferta() {
        erroRecusarOferta = null
        idOfertaRecusada = null
    }

    // Envia a recusa da oferta
    fun recusarOferta(idOferta: Int) {

        // Impede requisições duplicadas
        if (recusandoOferta || aceitandoOferta) return

        recusandoOferta = true
        limparEstadoRecusarOferta()

        viewModelScope.launch {
            try {
                val resposta = coletaService.recusarOferta(idOferta = idOferta)

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {

                        // Remove imediatamente a oferta recusada da lista exibida
                        coletas = coletas.filterNot {
                            it.idOfertaInicial == idOferta
                        }

                        // Informa à interface que a recusa funcionou
                        idOfertaRecusada = idOferta

                        // Atualiza os cards a partir do backend
                        carregarColetas()

                    } else {
                        erroRecusarOferta = corpo?.message ?: "Não foi possível recusar a oferta."
                    }
                } else {
                    erroRecusarOferta = when (resposta.code()) {
                        400 -> "O ID da oferta é inválido."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "A oferta não foi encontrada."
                        409 -> "Esta oferta não pode ser recusada no estado atual."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao recusar oferta. Código: ${resposta.code()}"
                    }
                }
            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                erroRecusarOferta = "Não foi possível concluir a recusa. Verifique sua conexão."

            } finally {
                recusandoOferta = false
            }
        }
    }

    // Inicialização
    init {
        // Busca as ofertas ao criar a ViewModel
        carregarColetas()
    }
}