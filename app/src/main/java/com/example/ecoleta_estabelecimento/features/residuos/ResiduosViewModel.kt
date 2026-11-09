package com.example.ecoleta_estabelecimento.features.residuos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ecoleta_estabelecimento.features.residuos.model.Residuo

class ResiduosViewModel : ViewModel() {

    var busca by mutableStateOf("")
        private set

    fun atualizarBusca(novaBusca: String) {
        busca = novaBusca
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
            idStatusResiduo = 1,
            statusResiduo = "Agendado",
            statusCard = "AGENDADO"
        )
    )

    val residuosFiltrados: List<Residuo>
        get() = residuos.filter { residuo ->
            residuo.material.contains(
                busca,
                ignoreCase = true
            )
        }
}