package com.example.ecoleta_estabelecimento.features.perfil.model

import com.google.gson.annotations.SerializedName

data class PerfilSenha(
    @SerializedName("senha_atual")
    val senhaAtual: String,

    @SerializedName("nova_senha")
    val novaSenha: String,

    @SerializedName("confirmar_senha")
    val confirmarSenha: String
)

data class PerfilSenhaResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String?
)