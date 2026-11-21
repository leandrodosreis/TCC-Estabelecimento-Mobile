package com.example.ecoleta_estabelecimento.features.historico

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoleta_estabelecimento.features.historico.components.DetalhesHistorico
import com.example.ecoleta_estabelecimento.features.historico.components.TipoHistorico
import com.example.ecoleta_estabelecimento.features.historico.model.HistoricoEstabelecimento
import com.example.ecoleta_estabelecimento.features.historico.service.HistoricoService
import com.example.ecoleta_estabelecimento.features.residuos.components.MaskResiduo.formatarQuantidade
import com.example.ecoleta_estabelecimento.retrofit.RetrofitFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

// Formata a quantidade retornada pelo backend
private fun formatarQuantidade(quantidade: String): String {
    val texto = quantidade.trim()

    if (texto.endsWith("kg", ignoreCase = true)) {
        return texto
    }

    val numero = texto.replace(',', '.').toBigDecimalOrNull()
        ?: return texto

    return "${numero.stripTrailingZeros().toPlainString().replace('.', ',')} kg"
}

// Formata o valor acordado em reais
private fun formatarValor(valor: String): String {
    val texto = valor.trim()

    if (texto.startsWith("R$")) {
        return texto
    }

    val numero = texto.replace(',', '.').toBigDecimalOrNull()
        ?: return texto

    return NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("pt-BR")
    ).format(numero)
}

class HistoricoViewModel : ViewModel() {
    // Serviço responsável pelas requisições HTTP do histórico
    private val historicoService = RetrofitFactory.criarServico(HistoricoService::class.java)

    // Lista de registros do histórico retornados pelo backend
    var registros by mutableStateOf<List<HistoricoEstabelecimento>>(emptyList())
        private set

    // Indica se o histórico está sendo carregado
    var carregandoHistorico by mutableStateOf(false)
        private set

    // Guarda um possível erro na consulta do histórico
    var erroCarregamentoHistorico by mutableStateOf<String?>(null)
        private set

    // Busca o histórico do estabelecimento autenticado
    fun carregarHistorico() {
        // Evita executar a mesma consulta simultaneamente
        if (carregandoHistorico) return

        carregandoHistorico = true
        erroCarregamentoHistorico = null

        viewModelScope.launch {
            try {
                // Realiza o GET utilizando o Retrofit
                val resposta = historicoService.listarHistorico()

                if (resposta.isSuccessful) {
                    val corpo = resposta.body()

                    if (corpo?.status == true) {
                        // Só atualiza o estado quando a lista mudar
                        if (registros != corpo.response.resumo) {
                            registros = corpo.response.resumo
                        }
                    } else {
                        erroCarregamentoHistorico = "Não foi possível carregar o histórico."
                    }
                } else {
                    erroCarregamentoHistorico = when (resposta.code()) {
                        400 -> "Não foi possível consultar o histórico."
                        401 -> "Sua autenticação expirou ou é inválida."
                        404 -> {
                            // Estabelecimento ainda não possui registros no histórico
                            registros = emptyList()
                            null
                        }

                        500 -> "Ocorreu um erro interno no servidor."
                        else -> "Erro ao carregar histórico. Código: ${resposta.code()}"
                    }
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                erroCarregamentoHistorico = "Não foi possível conectar ao servidor."
            } finally {
                // Finaliza o carregamento mesmo em caso de erro
                carregandoHistorico = false
            }
        }
    }

    // Guarda os detalhes do registro selecionado
    var detalhesHistorico by mutableStateOf<DetalhesHistorico?>(null)
        private set

    // Indica se a consulta dos detalhes está em andamento
    var carregandoDetalhesHistorico by mutableStateOf(false)
        private set

    // Guarda possíveis erros na consulta dos detalhes
    var erroDetalhesHistorico by mutableStateOf<String?>(null)
        private set

    // Guarda a consulta atual para evitar resultados de registros anteriores
    private var tarefaDetalhes: Job? = null

    // Limpa os dados de uma consulta anterior
    fun limparDetalhesHistorico() {
        tarefaDetalhes?.cancel()
        tarefaDetalhes = null

        detalhesHistorico = null
        erroDetalhesHistorico = null
        carregandoDetalhesHistorico = false
    }

    // Busca os detalhes do registro selecionado no histórico
    fun buscarDetalhesHistorico(registro: HistoricoEstabelecimento) {
        // Cancela uma consulta anterior, caso exista
        limparDetalhesHistorico()

        carregandoDetalhesHistorico = true

        tarefaDetalhes = viewModelScope.launch {
            try {
                // Identifica se o registro possui uma coleta concluída
                if (registro.idColeta != null) {

                    // Busca os detalhes da coleta concluída
                    val resposta = historicoService.buscarColetaConcluida(
                        registro.idColeta
                    )

                    if (resposta.isSuccessful) {
                        val corpo = resposta.body()

                        if (corpo?.status == true) {
                            val dados = corpo.response.resumo.firstOrNull()

                            if (dados != null) {
                                // Prepara os dados para o popup da coleta
                                detalhesHistorico = DetalhesHistorico(
                                    tipo = TipoHistorico.CONCLUIDA,
                                    material = dados.material,
                                    quantidade = formatarQuantidade(dados.quantidade),
                                    data = dados.dataDisponivel,
                                    status = dados.statusColeta,
                                    nomeEmpresaColetora = dados.nomeEmpresaColetora,
                                    nomeOfertante = dados.nomeOfertante,
                                    nomeAceitante = dados.nomeAceitante,
                                    nomeColetor = dados.nomeColetor,
                                    placaVeiculo = dados.placaVeiculo,
                                    logradouro = dados.logradouro,
                                    numero = dados.numero.toString(),
                                    bairro = dados.bairro,
                                    cidade = dados.cidade,
                                    horarioInicial = dados.horarioInicial,
                                    horarioFinal = dados.horarioFinal,
                                    observacao = dados.observacao,
                                    valorAcordado = formatarValor(dados.valorAcordado),
                                    dataColeta = dados.dataColeta,
                                    horaColeta = dados.horaColeta
                                )
                            } else {
                                erroDetalhesHistorico = "Nenhum detalhe da coleta foi encontrado."
                            }
                        } else {
                            erroDetalhesHistorico =
                                "Não foi possível carregar os detalhes da coleta."
                        }
                    } else {
                        erroDetalhesHistorico = when (resposta.code()) {
                            400 -> "O ID da coleta é inválido."
                            401 -> "Sua autenticação expirou ou é inválida."
                            404 -> "A coleta não foi encontrada."
                            500 -> "Ocorreu um erro interno no servidor."
                            else -> "Erro ao consultar a coleta. Código: ${resposta.code()}"
                        }
                    }
                } else {

                    // Busca os detalhes do resíduo cancelado
                    val resposta = historicoService.buscarResiduoCancelado(
                        registro.idResiduo
                    )

                    if (resposta.isSuccessful) {
                        val corpo = resposta.body()

                        if (corpo?.status == true) {
                            val dados = corpo.response.resumo.firstOrNull()

                            if (dados != null) {
                                // Prepara os dados para o popup do cancelamento
                                detalhesHistorico = DetalhesHistorico(
                                    tipo = TipoHistorico.CANCELADO,
                                    material = dados.material,
                                    quantidade = formatarQuantidade(dados.quantidade),
                                    data = dados.dataDisponivel,
                                    status = dados.statusResiduo,
                                    logradouro = dados.logradouro,
                                    numero = dados.numero.toString(),
                                    bairro = dados.bairro,
                                    cidade = dados.cidade,
                                    horarioInicial = dados.horarioInicial,
                                    horarioFinal = dados.horarioFinal,
                                    observacao = dados.observacao
                                )
                            } else {
                                erroDetalhesHistorico = "Nenhum detalhe do resíduo foi encontrado."
                            }
                        } else {
                            erroDetalhesHistorico =
                                "Não foi possível carregar os detalhes do resíduo."
                        }
                    } else {
                        erroDetalhesHistorico = when (resposta.code()) {
                            400 -> "O ID do resíduo é inválido."
                            401 -> "Sua autenticação expirou ou é inválida."
                            404 -> "O resíduo não foi encontrado."
                            500 -> "Ocorreu um erro interno no servidor."
                            else -> "Erro ao consultar o resíduo. Código: ${resposta.code()}"
                        }
                    }
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                erroDetalhesHistorico = "Não foi possível conectar ao servidor."
            } finally {
                // Finaliza o carregamento dos detalhes
                carregandoDetalhesHistorico = false
            }
        }
    }

    // Guarda o texto digitado no campo de busca
    var busca by mutableStateOf("")
        private set

    // Atualiza o texto utilizado na pesquisa
    fun atualizarBusca(novaBusca: String) {
        busca = novaBusca
    }

    // Tipo de material selecionado no filtro
    // null significa que todos os materiais estão selecionados
    var tipoSelecionado by mutableStateOf<String?>(null)
        private set

    // Atualiza o tipo de material selecionado
    fun atualizarTipoSelecionado(tipo: String?) {
        tipoSelecionado = tipo
    }

    // Guarda o status selecionado no filtro
    // null significa que nenhum filtro de status está aplicado
    var statusSelecionado by mutableStateOf<String?>(null)
        private set

    // Atualiza o status selecionado pelo usuário
    fun atualizarStatusSelecionado(status: String?) {
        statusSelecionado = status
    }

    // Obtém os tipos de materiais existentes no histórico
    // distinct evita repetir materiais presentes em vários registros
    val tiposDisponiveis: List<String>
        get() = registros
            .map { it.material }
            .distinct()
            .sorted()

    // Lista de registros que correspondem à pesquisa e aos filtros
    val registrosFiltrados: List<HistoricoEstabelecimento>
        get() = registros.filter { registro ->

            // Verifica se o texto pesquisado corresponde ao registro
            val correspondeBusca = busca.isBlank() ||
                    registro.material.contains(busca, ignoreCase = true) ||
                    registro.nomeEstabelecimento.contains(busca, ignoreCase = true) ||
                    registro.nomeEmpresaColetora.orEmpty().contains(busca, ignoreCase = true) ||
                    registro.idResiduo.toString().contains(busca) ||
                    registro.idColeta?.toString()?.contains(busca) == true

            // Verifica se o material corresponde ao tipo selecionado
            val correspondeTipo = tipoSelecionado == null || registro.material
                .equals(
                    tipoSelecionado,
                    ignoreCase = true
                )

            // Verifica se o registro corresponde ao status selecionado
            // Quando o status é null, aceita qualquer registro
            val correspondeStatus = when (statusSelecionado) {
                "Concluída" -> registro.idColeta != null
                "Cancelado" -> registro.idColeta == null
                else -> true
            }

            // O registro precisa atender aos três critérios
            // texto pesquisado, tipo de material e status
            correspondeBusca && correspondeTipo && correspondeStatus
        }

    // Executado automaticamente quando a ViewModel é criada
    init {
        // Busca os registros do histórico do estabelecimento
        carregarHistorico()
    }
}