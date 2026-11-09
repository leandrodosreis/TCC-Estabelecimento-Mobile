package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class Residuo (
    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    @SerializedName("material")
    val material: String,

    @SerializedName("quantidade")
    val quantidade: String,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    @SerializedName("horario_inicial")
    val horarioInicial: String,

    @SerializedName("horario_final")
    val horarioFinal: String,

    @SerializedName("observacao")
    val observacao: String?,

    @SerializedName("id_status_residuo")
    val idStatusResiduo: Int,

    @SerializedName("status_residuo")
    val statusResiduo: String,

    @SerializedName("status_card")
    val statusCard: String
)