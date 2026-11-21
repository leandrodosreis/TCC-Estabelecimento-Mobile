package com.example.ecoleta_estabelecimento.features.cadastro.model

import com.google.gson.annotations.SerializedName

// Representa os dados enviados ao backend para cadastrar um estabelecimento.
data class CadastroEstabelecimentoRequest(
    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("cnpj")
    val cnpj: String,

    // Deve ser enviado somente com números, sem hífen.
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
    val numeroLocal: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("telefone")
    val telefone: String,

    @SerializedName("senha")
    val senha: String,

    @SerializedName("confirmar_senha")
    val confirmarSenha: String
)