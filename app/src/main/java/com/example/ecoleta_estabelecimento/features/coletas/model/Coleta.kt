package com.example.ecoleta_estabelecimento.features.coletas.model

import com.google.gson.annotations.SerializedName

// OFERTAS
data class Coleta(
    @SerializedName("id_empresa_coletora")
    val idEmpresaColetora: Int,

    @SerializedName("nome_empresa_coletora")
    val nomeEmpresaColetora: String,

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

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

    @SerializedName("horario_inicial")
    val horarioInicial: String,

    @SerializedName("horario_final")
    val horarioFinal: String,

    @SerializedName("id_oferta_inicial")
    val idOfertaInicial: Int,

    @SerializedName("valor_ofertado")
    val valorOfertado: String,

    @SerializedName("status_oferta")
    val statusOferta: String
)

// GET DE OFERTAS
data class ColetasResumo(

    @SerializedName("resumo")
    val resumo: List<Coleta>
)

data class ColetasResponse(

    @SerializedName("response")
    val response: ColetasResumo,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int
)

// BODY DE ACEITAR A OFERTA
data class AceitarOfertaRequest(

    @SerializedName("nome_aceitante")
    val nomeAceitante: String
)

// ACEITAR OFERTA
data class AceitarOfertaDados(

    @SerializedName("id_oferta_inicial")
    val idOfertaInicial: Int,

    @SerializedName("id_oferta_aceita")
    val idOfertaAceita: Int,

    @SerializedName("id_oferta_final")
    val idOfertaFinal: Int,

    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_coleta")
    val idColeta: Int,

    @SerializedName("nome_aceitante")
    val nomeAceitante: String,

    @SerializedName("status_oferta")
    val statusOferta: String,

    @SerializedName("status_residuo")
    val statusResiduo: String,

    @SerializedName("status_coleta")
    val statusColeta: String,

    @SerializedName("ofertas_recusadas")
    val ofertasRecusadas: Int
)

data class AceitarOfertaResponse(

    @SerializedName("response")
    val response: AceitarOfertaDados,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String?
)

// RECUSAR OFERTA
data class RecusarOfertaDados(

    @SerializedName("id_oferta_inicial")
    val idOfertaInicial: Int,

    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_historico_oferta")
    val idHistoricoOferta: Int,

    @SerializedName("status_oferta")
    val statusOferta: String
)

data class RecusarOfertaResponse(

    @SerializedName("response")
    val response: RecusarOfertaDados,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String?
)