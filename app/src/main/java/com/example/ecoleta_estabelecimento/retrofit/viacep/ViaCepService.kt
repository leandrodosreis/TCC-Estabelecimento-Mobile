package com.example.ecoleta_estabelecimento.retrofit.viacep

import retrofit2.http.GET
import retrofit2.http.Path

// Serviço independente da API principal do Ecoleta
interface ViaCepService {
    // Consulta um CEP com oito dígitos
    @GET("ws/{cep}/json/")
    suspend fun consultarCep(
        @Path("cep") cep: String
    ): ViaCepResponse
}