package com.example.ecoleta_estabelecimento.features.historico.model

import com.google.gson.annotations.SerializedName

// Detalhes do cancelamento
data class HistoricoResiduoCancelado(

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    val material: String,

    @SerializedName("id_status_residuo")
    val idStatusResiduo: Int,

    @SerializedName("status_residuo")
    val statusResiduo: String,

    val logradouro: String,

    val numero: Int,

    val bairro: String,

    val cidade: String,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    val quantidade: String,

    @SerializedName("horario_inicial")
    val horarioInicial: String,

    @SerializedName("horario_final")
    val horarioFinal: String,

    val observacao: String?
)

// Conteúdo da resposta
data class HistoricoResiduoCanceladoConteudo(
    val resumo: List<HistoricoResiduoCancelado>
)

// Resposta do cancelamento
data class HistoricoResiduoCanceladoResponse(
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    val response: HistoricoResiduoCanceladoConteudo
)

