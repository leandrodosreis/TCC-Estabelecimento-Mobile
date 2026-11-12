package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class ResiduoCancelarResponse (
    val status: Boolean,
    val message: String?,
    @SerializedName("status_code")
    val statusCode: Int?,
    val response: ResiduoCanceladoDetalhes?
)

data class ResiduoCanceladoDetalhes(
    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_historico")
    val idHistorico: Int?,

    @SerializedName("status_residuo")
    val statusResiduo: String?
)