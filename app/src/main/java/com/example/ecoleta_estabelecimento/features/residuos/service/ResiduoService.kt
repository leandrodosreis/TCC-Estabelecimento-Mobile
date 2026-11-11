package com.example.ecoleta_estabelecimento.features.residuos.service

import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ResiduoService {
    @POST("estabelecimento/residuos")
    suspend fun cadastrarResiduo(
        @Body residuo: ResiduoCadastroRequest
    ): Response<ResiduoCadastroResponse>
}