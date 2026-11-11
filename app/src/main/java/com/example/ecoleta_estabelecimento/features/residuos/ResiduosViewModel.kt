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

        // Inicia o estado de carregamento.
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
                        // Confirma o cadastro somente após sucesso da API
                        cadastroResiduoRealizado = true
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
                erroCadastroResiduo = "Não foi possível conectar ao servidor. Verifique sua conexão."
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

    val residuos = listOf(
        Residuo(
            idResiduo = 1,
            idEstabelecimento = 1,
            idTipoMaterial = 1,
            material = "Vidro",
            quantidade = "35",
            dataDisponivel = "28/08/2026",
            horarioInicial = "08:00",
            horarioFinal = "12:00",
            observacao = "Material separado e embalado.",
            idStatusResiduo = 1,
            statusResiduo = "Disponível",
            statusCard = "DISPONÍVEL"
        ),
        Residuo(
            idResiduo = 2,
            idEstabelecimento = 1,
            idTipoMaterial = 2,
            material = "Papelão",
            quantidade = "18",
            dataDisponivel = "28/08/2026",
            horarioInicial = "08:00",
            horarioFinal = "12:00",
            observacao = "Próximo ao local indicado.",
            idStatusResiduo = 2,
            statusResiduo = "Agendado",
            statusCard = "AGENDADO"
        ),
        Residuo(
            idResiduo = 3,
            idEstabelecimento = 1,
            idTipoMaterial = 3,
            material = "Plástico",
            quantidade = "12",
            dataDisponivel = "29/08/2026",
            horarioInicial = "08:00",
            horarioFinal = "17:00",
            observacao = "Material leve, porém grande.",
            idStatusResiduo = 1,
            statusResiduo = "Disponível",
            statusCard = "DISPONÍVEL"
        ),
        Residuo(
            idResiduo = 4,
            idEstabelecimento = 1,
            idTipoMaterial = 6,
            material = "Papel",
            quantidade = "12",
            dataDisponivel = "09/11/2026",
            horarioInicial = "08:00",
            horarioFinal = "17:00",
            observacao = "",
            idStatusResiduo = 2,
            statusResiduo = "Agendado",
            statusCard = "AGENDADO"
        )
    )

    val tiposMateriais = listOf(
        TipoMaterial(1, "Vidro"),
        TipoMaterial(2, "Papelão"),
        TipoMaterial(3, "Plástico"),
        TipoMaterial(4, "Metal")
    )

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
            .map { residuo ->
                // Primeiro valor: ID; segundo valor: nome do status
                residuo.idStatusResiduo to residuo.statusResiduo
            }

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
}