package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class ResiduoCadastroRequest (

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    @SerializedName("quantidade")
    val quantidade: Double,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    @SerializedName("horario_inicial")
    val horarioInicial: String,

    @SerializedName("horario_final")
    val horarioFinal: String,

    @SerializedName("observacao")
    val observacao: String? = null
)