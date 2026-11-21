package com.example.ecoleta_estabelecimento.features.perfil.model

import com.google.gson.annotations.SerializedName

data class PerfilAtualizar(
    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("telefone")
    val telefone: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("cep")
    val cep: String,

    @SerializedName("logradouro")
    val logradouro: String,

    @SerializedName("bairro")
    val bairro: String,

    @SerializedName("cidade")
    val cidade: String,

    @SerializedName("nome_estado")
    val nomeEstado: String,

    @SerializedName("sigla_estado")
    val siglaEstado: String,

    @SerializedName("numero_local")
    val numeroLocal: Int
)

data class PerfilAtualizadoDados(
    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("telefone")
    val telefone: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("cep")
    val cep: String,

    @SerializedName("logradouro")
    val logradouro: String,

    @SerializedName("numero")
    val numero: Int,

    @SerializedName("bairro")
    val bairro: String,

    @SerializedName("cidade")
    val cidade: String,

    @SerializedName("nome_estado")
    val nomeEstado: String,

    @SerializedName("sigla_estado")
    val siglaEstado: String
)

data class PerfilAtualizarResponse(
    @SerializedName("response")
    val response: PerfilAtualizadoDados?,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String?
)