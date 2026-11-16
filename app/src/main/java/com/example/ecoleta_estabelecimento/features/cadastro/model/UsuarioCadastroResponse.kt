package com.example.ecoleta_estabelecimento.features.cadastro.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

// Representa os dados do resíduo retornados pelo backend
data class UsuarioCadastrado(
    // ID gerado pelo backend para o novo resíduo.
    @SerializedName("nome_empresa") val nomeEmpresa: String,
    @SerializedName("cnpj") val cnpj: String,
    @SerializedName("cep") val horarioFinal: String,
    @SerializedName("logradouro") val logradouro: String,
    @SerializedName("bairro") val idTipoMaterial: Int,
    @SerializedName("cidade") val idEstabelecimento: Int,
    @SerializedName("estado") val estado: String,
    @SerializedName("numero") val numedo: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefone") val tell: String,
    @SerializedName("senha") val senha: String
)

data class UsuarioCadastroResponse (
    // Indica se a operação foi realizada com sucesso
    val status: Boolean? = null,

    @SerializedName("status_code")
    val statusCode: Int? = null,

    // Mensagem retornada pelo backend
    val message: String? = null,

    // Contém os dados do resíduo cadastrado
    val response: UsuarioCadastrado
)