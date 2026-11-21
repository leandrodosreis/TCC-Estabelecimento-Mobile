package com.example.ecoleta_estabelecimento.features.historico.service

import com.example.ecoleta_estabelecimento.features.historico.model.HistoricoColetaConcluidaResponse
import com.example.ecoleta_estabelecimento.features.historico.model.HistoricoEstabelecimentoResponse
import com.example.ecoleta_estabelecimento.features.historico.model.HistoricoResiduoCanceladoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoricoService {
    // GET - Listar histórico
    @GET("estabelecimento/historico")
    suspend fun listarHistorico(): Response<HistoricoEstabelecimentoResponse>

    // GET - Resíduo cancelado
    @GET("estabelecimento/historico/cancelado/{idResiduo}")
    suspend fun buscarResiduoCancelado(
        @Path("idResiduo") idResiduo: Int
    ): Response<HistoricoResiduoCanceladoResponse>

    // GET - Coleta concluída
    @GET("estabelecimento/historico/concluida/{idColeta}")
    suspend fun buscarColetaConcluida(
        @Path("idColeta") idColeta: Int
    ): Response<HistoricoColetaConcluidaResponse>
}