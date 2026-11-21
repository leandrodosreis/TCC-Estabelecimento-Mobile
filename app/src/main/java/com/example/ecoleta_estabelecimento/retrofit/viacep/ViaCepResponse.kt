package com.example.ecoleta_estabelecimento.retrofit.viacep

// Representa os campos necessários do retorno do ViaCEP
data class ViaCepResponse(
    val cep: String? = null,
    val logradouro: String? = null,
    val bairro: String? = null,
    // A API chama a cidade de "localidade"
    val localidade: String? = null,
    // Sigla do estado, por exemplo "SP"
    val uf: String? = null,
    // Nome completo do estado
    val estado: String? = null,
    // CEP inexistente pode retornar erro = true
    val erro: Boolean? = null
)