package com.example.ecoleta_estabelecimento.features.historico.model

import com.google.gson.annotations.SerializedName

// Registro do histórico
data class HistoricoEstabelecimento(

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("nome_estabelecimento")
    val nomeEstabelecimento: String,

    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_coleta")
    val idColeta: Int?,

    @SerializedName("id_oferta_inicial")
    val idOfertaInicial: Int?,

    @SerializedName("tipo_registro")
    val tipoRegistro: String,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    @SerializedName("id_empresa_coletora")
    val idEmpresaColetora: Int?,

    @SerializedName("nome_empresa_coletora")
    val nomeEmpresaColetora: String?,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    val material: String,

    val quantidade: String,

    @SerializedName("valor_acordado")
    val valorAcordado: String?,

    @SerializedName("id_status_residuo")
    val idStatusResiduo: Int?,

    @SerializedName("id_status_coleta")
    val idStatusColeta: Int?,

    @SerializedName("status_historico")
    val statusHistorico: String
)

// Conteúdo da resposta
data class HistoricoEstabelecimentoConteudo(
    val resumo: List<HistoricoEstabelecimento>
)

// Resposta do histórico
data class HistoricoEstabelecimentoResponse(
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    val response: HistoricoEstabelecimentoConteudo
)