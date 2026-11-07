package com.example.ecoleta_estabelecimento.features.home.model

import com.google.gson.annotations.SerializedName

data class HomeDados (
    @SerializedName("resumo")
    val resumo: HomeResumo,

    @SerializedName("ofertas")
    val ofertas: List<OfertaHome>
)

data class OfertaHome (
    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("id_empresa_coletora")
    val idEmpresaColetora: Int,

    @SerializedName("nome_empresa_coletora")
    val nomeEmpresaColetora: String,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    @SerializedName("material")
    val material: String,

    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("quantidade")
    val quantidade: String,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    @SerializedName("id_oferta_inicial")
    val idOfertaInicial: Int,

    @SerializedName("valor_ofertado")
    val valorOfertado: String,

    @SerializedName("status_oferta")
    val statusOferta: String
)

data class HomeResponse (
    @SerializedName("response")
    val response: HomeDados,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int
)