package com.example.ecoleta_estabelecimento.features.cadastro.model

import com.google.gson.annotations.SerializedName

data class ViaCepResponse(
    @SerializedName("logradouro") val logradouro: String? = null,
    @SerializedName("bairro") val bairro: String? = null,
    @SerializedName("localidade") val cidade: String? = null,
    @SerializedName("uf") val uf: String? = null,
    // O ViaCEP responde {"erro": true} quando o CEP não existe
    @SerializedName("erro") val erro: Boolean? = null
)