package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class MeusResiduosResponse (
    // Lista dos resíduos cadastrados pelo estabelecimento
    @SerializedName("response")
    val response: List<Residuo>,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int
)