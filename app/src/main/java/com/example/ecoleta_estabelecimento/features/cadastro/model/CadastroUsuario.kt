package com.example.ecoleta_estabelecimento.features.cadastro.model

import com.google.gson.annotations.SerializedName

data class CadastrarUsuario (
    @SerializedName("nome_empresa") val nomeEmpresa: String,

    @SerializedName("cnpj") val cnpj: String,

    @SerializedName("cep") val cep: String,

    @SerializedName("logradouro") val logradouro: String,

    @SerializedName("bairro") val bairro: String,

    @SerializedName("cidade") val cidade: String,

    @SerializedName("nome_estado") val nomeEstado: String,

    @SerializedName("sigla_estado") val estado: String,

    @SerializedName("numero_local") val numero: Int,

    @SerializedName("email") val email: String,

    @SerializedName("telefone") val telefone: String,

    @SerializedName("senha") val senha: String,

    @SerializedName("confirmar_senha") val confirmarSenha: String
)