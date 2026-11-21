package com.example.ecoleta_estabelecimento.features.historico.model

import com.google.gson.annotations.SerializedName

// Detalhes da coleta concluída
data class HistoricoColetaConcluida(

    @SerializedName("id_coleta")
    val idColeta: Int,

    @SerializedName("id_estabelecimento")
    val idEstabelecimento: Int,

    @SerializedName("id_residuo")
    val idResiduo: Int,

    @SerializedName("id_tipo_material")
    val idTipoMaterial: Int,

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

    @SerializedName("nome_aceitante")
    val nomeAceitante: String,

    @SerializedName("nome_coletor")
    val nomeColetor: String,

    @SerializedName("placa_veiculo")
    val placaVeiculo: String,

    val logradouro: String,

    val numero: Int,

    val bairro: String,

    val cidade: String,

    val quantidade: String,

    @SerializedName("data_disponivel")
    val dataDisponivel: String,

    @SerializedName("horario_inicial")
    val horarioInicial: String,

    @SerializedName("horario_final")
    val horarioFinal: String,

    val observacao: String?,

    @SerializedName("valor_acordado")
    val valorAcordado: String,

    @SerializedName("data_coleta")
    val dataColeta: String,

    @SerializedName("hora_coleta")
    val horaColeta: String
)

// Conteúdo da resposta
data class HistoricoColetaConcluidaConteudo(
    val resumo: List<HistoricoColetaConcluida>
)

// Resposta da coleta concluída
data class HistoricoColetaConcluidaResponse(
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    val response: HistoricoColetaConcluidaConteudo
)

