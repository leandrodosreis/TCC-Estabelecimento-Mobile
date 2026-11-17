package com.example.ecoleta_estabelecimento.features.cadastro.service

import com.example.ecoleta_estabelecimento.features.cadastro.model.ViaCepResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("ws/{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): Response<ViaCepResponse>
}
