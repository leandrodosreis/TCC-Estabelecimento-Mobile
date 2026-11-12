package com.example.ecoleta_estabelecimento.features.residuos.model

import com.google.gson.annotations.SerializedName

data class DetalhesColetaResponse(
    @SerializedName("response")
    val response: DetalhesColeta,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int
)

data class DetalhesColeta(
    @SerializedName("id_coleta")
    val idColeta: Int,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

    @SerializedName("material")
    val material: String,

    @SerializedName("id_status_coleta")
    val idStatusColeta: Int,

    @SerializedName("status_coleta")
    val statusColeta: String,

    @SerializedName("id_empresa_coletora")
    val idEmpresaColetora: Int,

    @SerializedName("nome_empresa_coletora")
    val nomeEmpresaColetora: String,

    @SerializedName("nome_ofertante")
    val nomeOfertante: String,

    @SerializedName("id_oferta_aceita")
    val idOfertaAceita: Int,

    @SerializedName("nome_aceitante")
    val nomeAceitante: String,

    @SerializedName("logradouro")
    val logradouro: String,

    @SerializedName("numero")
    val numero: Int,

    @SerializedName("bairro")
    val bairro: String,

    @SerializedName("cidade")
    val cidade: String,

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

    @SerializedName("id_oferta_inicial")
    val idOfertaInicial: Int,

    @SerializedName("valor_acordado")
    val valorAcordado: String,

    @SerializedName("id_oferta_final")
    val idOfertaFinal: Int,

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("id_residuo")
    val idResiduo: Int
)