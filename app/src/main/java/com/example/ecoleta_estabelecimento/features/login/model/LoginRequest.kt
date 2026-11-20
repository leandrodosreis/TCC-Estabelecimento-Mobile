package com.example.ecoleta_estabelecimento.features.login.model

import com.google.gson.annotations.SerializedName

// Dados enviados ao backend quando o estabelecimento realiza o login
data class LoginRequest (
    // Será enviado sem os caracteres da máscara
    @SerializedName("cnpj")
    val cnpj: String,

    @SerializedName("senha")
    val senha: String
)