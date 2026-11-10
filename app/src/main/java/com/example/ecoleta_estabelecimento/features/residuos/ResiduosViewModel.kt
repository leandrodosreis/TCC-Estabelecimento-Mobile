package com.example.ecoleta_estabelecimento.features.residuos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo
import com.example.ecoleta_estabelecimento.features.residuos.model.TipoMaterial

class ResiduosViewModel : ViewModel() {

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