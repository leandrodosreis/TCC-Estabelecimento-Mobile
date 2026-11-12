package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class TiposMateriaisResponse(
    // Lista de materiais retornada dentro do campo "response"
    @SerializedName("response")
    val response: List<TipoMaterial>,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int
)