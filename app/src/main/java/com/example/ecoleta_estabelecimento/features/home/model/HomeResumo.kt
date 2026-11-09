package com.example.ecoleta_estabelecimento.features.home.model

import com.google.gson.annotations.SerializedName

data class HomeResumo (
    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("residuos_disponiveis")
    val residuosDisponiveis: Int,

    @SerializedName("soma_total_quantidade")
    val somaTotalQuantidade: String,

    @SerializedName("ofertas_recebidas")
    val ofertasRecebidas: Int,

    @SerializedName("coletas_agendadas")
    val coletasAgendadas: Int
)