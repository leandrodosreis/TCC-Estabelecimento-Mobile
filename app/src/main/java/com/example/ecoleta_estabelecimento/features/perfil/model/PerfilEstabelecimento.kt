package com.example.ecoleta_estabelecimento.features.perfil.model

import com.google.gson.annotations.SerializedName

data class PerfilEstabelecimento(
    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("cnpj")
    val cnpj: String,

    @SerializedName("id_telefone")
    val idTelefone: Int,

    @SerializedName("telefone")
    val telefone: String,

    @SerializedName("id_email")
    val idEmail: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("id_endereco")
    val idEndereco: Int,

    @SerializedName("cep")
    val cep: String,

    @SerializedName("logradouro")
    val logradouro: String,

    @SerializedName("numero")
    val numero: Int,

    @SerializedName("id_estado")
    val idEstado: Int,

    @SerializedName("sigla")
    val sigla: String,

    @SerializedName("nome_estado")
    val nomeEstado: String,

    @SerializedName("id_cidade")
    val idCidade: Int,

    @SerializedName("nome_cidade")
    val nomeCidade: String,

    @SerializedName("bairro")
    val bairro: String
)

data class PerfilEstabelecimentoConteudo(

    @SerializedName("count")
    val count: Int,

    @SerializedName("perfil")
    val perfil: List<PerfilEstabelecimento>
)

data class PerfilEstabelecimentoResponse(
    @SerializedName("response")
    val response: PerfilEstabelecimentoConteudo,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int
)