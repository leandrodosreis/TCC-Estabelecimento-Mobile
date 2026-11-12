package com.example.ecoleta_estabelecimento.features.residuos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.features.residuos.model.TipoMaterial
import com.example.ecoleta_estabelecimento.features.residuos.service.ResiduoService
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import kotlinx.coroutines.launch

class ResiduosViewModel : ViewModel() {

    // Serviço responsável pelas requisições HTTP dos resíduos
    private val residuoService = RetrofitFactory.criarServico(
        ResiduoService::class.java
    )

    // Indica se uma requisição de cadastro está em andamento
    var cadastrandoResiduo by mutableStateOf(false)
        private set

    // Mensagem de erro retornada durante o cadastro
    var erroCadastroResiduo by mutableStateOf<String?>(null)
        private set

    // Indica se o último cadastro foi realizado com sucesso
    var cadastroResiduoRealizado by mutableStateOf(false)
        private set

    // Envia os dados de um novo resíduo para o backend
    fun cadastrarResiduo(residuo: ResiduoCadastroRequest) {
        // Evita duas requisições simultâneas
        if (cadastrandoResiduo) return

        // Inicia o estado de carregamento
        cadastrandoResiduo = true

        // Limpa resultados de tentativas anteriores
        erroCadastroResiduo = null
        cadastroResiduoRealizado = false

        // Executa a requisição dentro da coroutine da ViewModel
        viewModelScope.launch {
            try {
                // Envia o POST para o backend
                val resposta = residuoService.cadastrarResiduo(residuo)

                if (resposta.isSuccessful) {
                    // Obtém o corpo retornado pelo backend
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Confirma o cadastro para exibir a animação
                        cadastroResiduoRealizado = true

                        // Busca novamente os resíduos para atualizar os cards
                        carregarResiduos()
                    } else {
                        // A API respondeu, mas não confirmou o sucesso
                        erroCadastroResiduo =
                            corpo?.message ?: "Não foi possível cadastrar o resíduo."
                    }
                } else {
                    // Trata os códigos HTTP retornados pelo backend
                    erroCadastroResiduo = when (resposta.code()) {
                        400 -> "Os dados informados são inválidos."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Não foi encontrado um registro necessário para o cadastro."
                        415 -> {
                            // Exibe o corpo da resposta para descobrir
                            // por que o backend rejeitou a requisição.
                            val detalheErro = resposta.errorBody()?.string()

                            android.util.Log.e(
                                "CADASTRO_RESIDUO",
                                "HTTP 415 - $detalheErro"
                            )

                            "Formato da requisição não aceito pelo servidor."
                        }

                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Não foi possível cadastrar o resíduo. Código: ${resposta.code()}"
                    }
                }
            } catch (e: Exception) {
                // Trata falhas de conexão e outros erros inesperados
                erroCadastroResiduo =
                    "Não foi possível conectar ao servidor. Verifique sua conexão."
            } finally {

                // Finaliza o carregamento em qualquer resultado.
                cadastrandoResiduo = false
            }
        }
    }

    // Limpa os estados do cadastro para uma nova tentativa ou para quando o usuário abrir o formulário novamente.
    fun limparEstadoCadastroResiduo() {
        erroCadastroResiduo = null
        cadastroResiduoRealizado = false
    }

    var busca by mutableStateOf("")
        private set

    fun atualizarBusca(novaBusca: String) {
        busca = novaBusca
    }

    // Tipo de material selecionado no filtro
    // null significa que todos os materiais estão selecionados
    var idTipoSelecionado by mutableStateOf<Int?>(null)
        private set

    // Atualiza o tipo de material selecionado
    fun atualizarTipoSelecionado(idTipo: Int?) {
        idTipoSelecionado = idTipo
    }

    // Guarda o ID do status selecionado no filtro
    // null significa que nenhum filtro de status está aplicado
    var idStatusSelecionado by mutableStateOf<Int?>(null)
        private set

    // Atualiza o status selecionado pelo usuário
    fun atualizarStatusSelecionado(idStatus: Int?) {
        idStatusSelecionado = idStatus
    }

    // Lista de resíduos recebidos do backend
    // O Compose atualiza a interface quando esta lista muda
    var residuos by mutableStateOf<List<Residuo>>(emptyList())
        private set


    // Indica se os resíduos estão sendo carregados
    var carregandoResiduos by mutableStateOf(false)
        private set

    // Guarda um possível erro na consulta dos resíduos
    var erroCarregamentoResiduos by mutableStateOf<String?>(null)
        private set

    // Busca os resíduos cadastrados pelo estabelecimento autenticado
    fun carregarResiduos() {
        // Evita executar a mesma consulta simultaneamente
        if (carregandoResiduos) return

        carregandoResiduos = true
        erroCarregamentoResiduos = null

        viewModelScope.launch {
            try {
                // Realiza o GET utilizando o Retrofit
                val resposta = residuoService.listarMeusResiduos()

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Atualiza a lista observada pelo Compose
                        residuos = corpo.response
                    } else {
                        erroCarregamentoResiduos = "Não foi possível carregar os resíduos."
                    }
                } else {
                    erroCarregamentoResiduos = when (resposta.code()) {
                        400 -> "Não foi possível consultar os resíduos."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Nenhum resíduo foi encontrado."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao carregar resíduos. Código: ${resposta.code()}"
                    }
                }
            } catch (e: Exception) {
                erroCarregamentoResiduos = "Não foi possível conectar ao servidor."
            } finally {
                // Finaliza o carregamento mesmo em caso de erro
                carregandoResiduos = false
            }
        }
    }

    // Lista de materiais recebidos do backend
    var tiposMateriais by mutableStateOf<List<TipoMaterial>>(emptyList())
        private set

    // Indica se os tipos de materiais estão sendo carregados
    var carregandoTiposMateriais by mutableStateOf(false)
        private set

    // Guarda um possível erro na consulta dos materiais
    var erroCarregamentoTiposMateriais by mutableStateOf<String?>(null)
        private set

    // Busca todos os tipos de materiais cadastrados no sistema
    fun carregarTiposMateriais() {

        // Impede consultas simultâneas
        if (carregandoTiposMateriais) return

        carregandoTiposMateriais = true
        erroCarregamentoTiposMateriais = null

        viewModelScope.launch {
            try {
                val resposta = residuoService.listarTiposMateriais()

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Atualiza a lista utilizada pelos dropdowns
                        tiposMateriais = corpo.response
                    } else {
                        erroCarregamentoTiposMateriais =
                            "Não foi possível carregar os tipos de materiais."
                    }
                } else {
                    erroCarregamentoTiposMateriais = when (resposta.code()) {
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> "Nenhum tipo de material foi encontrado."
                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao carregar materiais. Código: ${resposta.code()}"
                    }
                }
            } catch (e: Exception) {
                erroCarregamentoTiposMateriais = "Não foi possível conectar ao servidor."
            } finally {
                carregandoTiposMateriais = false
            }
        }
    }

    // Obtém os status existentes nos resíduos
    // O ID será usado na filtragem e o nome será exibido no dropdown
    // distinctBy evita repetir status que aparecem em vários resíduos
    val statusDisponiveis: List<Pair<Int, String>>
        get() = residuos
            .filter {
                // Nesta tela, exibimos apenas Disponível e Agendado
                it.statusResiduo.equals("Disponível", ignoreCase = true) ||
                        it.statusResiduo.equals("Agendado", ignoreCase = true)
            }
            .distinctBy { it.idStatusResiduo }
            .map { it.idStatusResiduo to it.statusCard }

    val residuosFiltrados: List<Residuo>
        get() = residuos.filter { residuo ->

            // Verifica se o material corresponde ao texto pesquisado
            val correspondeBusca = residuo.material.contains(
                busca,
                ignoreCase = true
            )

            // Verifica se o material corresponde ao tipo selecionado
            val correspondeTipo =
                idTipoSelecionado == null ||
                        residuo.idTipoMaterial == idTipoSelecionado

            // Verifica se o resíduo corresponde ao status selecionado
            // Quando o ID é null, aceita qualquer status
            val correspondeStatus =
                idStatusSelecionado == null ||
                        residuo.idStatusResiduo == idStatusSelecionado

            // O resíduo precisa atender aos três critérios:
            // texto pesquisado, tipo de material e status
            correspondeBusca && correspondeTipo && correspondeStatus
        }


    // Executado automaticamente quando a ViewModel é criada
    init {
        // Busca os resíduos cadastrados pelo estabelecimento
        carregarResiduos()

        // Busca todos os tipos de materiais disponíveis
        carregarTiposMateriais()
    }
}