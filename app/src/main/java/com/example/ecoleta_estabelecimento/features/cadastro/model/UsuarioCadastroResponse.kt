package com.example.ecoleta_estabelecimento.features.cadastro.model

import com.google.gson.annotations.SerializedName

data class UsuarioCadastroResponse (
    // Indica se a operação foi realizada com sucesso
    val status: Boolean,

    @SerializedName("status_code")
    val statusCode: Int,

    // Mensagem retornada pelo backend
    val message: String?,

    // Contém os dados do resíduo cadastrado
    val response: UsuarioCadastrado
)

// Representa os dados do resíduo retornados pelo backend
data class UsuarioCadastrado(
    // ID gerado pelo backend para o novo resíduo.
    @SerializedName("nome_empresa")
    val nomeEmpresa: String,

    @SerializedName("cnpj")
    val cnpj: String,

    @SerializedName("cep")
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