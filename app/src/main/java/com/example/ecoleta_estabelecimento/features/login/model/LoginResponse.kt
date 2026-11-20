package com.example.ecoleta_estabelecimento.features.login.model

import com.google.gson.annotations.SerializedName

// Representa os dados internos do campo "response"
data class LoginDados(
    // ID do estabelecimento autenticado
    @SerializedName("id")
    val id: Int,

    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("cnpj")
    val cnpj: String,

    // JWT utilizado para autenticar as próximas requisições
    @SerializedName("token")
    val token: String
)

// Resposta completa da API após a tentativa de autenticação
data class LoginResponse (
    // Contém os dados do estabelecimento autenticado e o JWT fornecido pelo backend
    @SerializedName("response")
    val response: LoginDados?,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String?
)