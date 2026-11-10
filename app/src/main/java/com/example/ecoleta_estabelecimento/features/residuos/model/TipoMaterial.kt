package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class TipoMaterial (
    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    @SerializedName("material")
    val material: String
)