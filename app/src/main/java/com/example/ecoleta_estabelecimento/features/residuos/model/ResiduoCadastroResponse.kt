package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class ResiduoCadastroResponse (
    // Indica se a operação foi realizada com sucesso
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    // Mensagem retornada pelo backend
    val message: String?,

    // Contém os dados do resíduo cadastrado
    val response: ResiduoCadastrado
)

// Representa os dados do resíduo retornados pelo backend
data class ResiduoCadastrado(
    // ID gerado pelo backend para o novo resíduo.
    val id: Int,

    @SerializedName("horario_inicial")
    val horarioInicial: String,

    @SerializedName("horario_final")
    val horarioFinal: String,

    val quantidade: Double,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    val observacao: String?,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("status_residuo")
    val statusResiduo: String
)