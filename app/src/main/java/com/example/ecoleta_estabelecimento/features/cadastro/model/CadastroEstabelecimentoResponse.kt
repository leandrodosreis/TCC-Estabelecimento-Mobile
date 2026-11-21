package com.example.ecoleta_estabelecimento.features.cadastro.model

import com.google.gson.annotations.SerializedName

// Representa os dados do estabelecimento cadastrado
data class EstabelecimentoCadastrado(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("nome_empresa")
    val nomeEmpresa: String? = null,

    @SerializedName("cnpj")
    val cnpj: String? = null
)

// Representa a resposta completa retornada pelo backend
data class CadastroEstabelecimentoResponse(

    @SerializedName("api_description")
    val apiDescription: String? = null,

    @SerializedName("development")
    val development: String? = null,

    @SerializedName("version")
    val version: String? = null,

    // Dados do estabelecimento cadastrado
    @SerializedName("response")
    val response: EstabelecimentoCadastrado? = null,

    @SerializedName("status")
    val status: Boolean? = null,

    @SerializedName("status_code")
    val statusCode: Int? = null,

    @SerializedName("message")
    val message: String? = null
)