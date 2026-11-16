package com.example.ecoleta_estabelecimento.features.coletas.service

import com.example.ecoleta_estabelecimento.features.coletas.model.AceitarOfertaRequest
import com.example.ecoleta_estabelecimento.features.coletas.model.AceitarOfertaResponse
import com.example.ecoleta_estabelecimento.features.coletas.model.ColetasResponse
import com.example.ecoleta_estabelecimento.features.coletas.model.RecusarOfertaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ColetaService {
    // Retorna ofertas pendentes e aceitas do estabelecimento autenticado
    @GET("estabelecimento/coletas")
    suspend fun listarColetas(): Response<ColetasResponse>

    // Busca os detalhes de uma oferta
    @GET("estabelecimento/coletas/{id_oferta_inicial}")
    suspend fun buscarDetalhesColeta(
        @Path("id_oferta_inicial")
        idOfertaInicial: Int
    ): Response<ColetasResponse>

    // Envia o nome do funcionário e agenda a coleta após o aceite
    @POST("estabelecimento/ofertas/{id_oferta}/aceitar")
    suspend fun aceitarOferta(
        @Path("id_oferta")
        idOferta: Int,

        @Body
        dados: AceitarOfertaRequest
    ): Response<AceitarOfertaResponse>

    // Recusa uma oferta
    @PATCH("estabelecimento/ofertas/{id_oferta}/recusar")
    suspend fun recusarOferta(
        @Path("id_oferta")
        idOferta: Int
    ): Response<RecusarOfertaResponse>
}