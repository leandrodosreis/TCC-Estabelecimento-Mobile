package com.example.ecoleta_estabelecimento.features.home.service

import com.example.ecoleta_estabelecimento.features.home.model.HomeResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeService {
    // Busca os dados da Home do estabelecimento autenticado
    @GET("estabelecimento/home")
    // Permite executar a requisição dentro de uma coroutine, que será iniciada pela ViewModel
        // coroutine é parecido com o async/await
    suspend fun buscarHome(): Response<HomeResponse>
}