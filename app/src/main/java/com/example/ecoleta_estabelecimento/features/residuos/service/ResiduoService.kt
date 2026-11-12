package com.example.ecoleta_estabelecimento.features.residuos.service

import com.example.ecoleta_estabelecimento.features.residuos.model.MeusResiduosResponse
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroResponse
import com.example.ecoleta_estabelecimento.features.residuos.model.TiposMateriaisResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ResiduoService {
    // Cadastra um novo resíduo
    @POST("estabelecimento/residuos")
    suspend fun cadastrarResiduo(
        @Body residuo: ResiduoCadastroRequest
    ): Response<ResiduoCadastroResponse>

    // Busca os resíduos do estabelecimento autenticado
    @GET("estabelecimento/meus-residuos")
    suspend fun listarMeusResiduos(): Response<MeusResiduosResponse>

    // Busca todos os tipos de materiais cadastrados no sistema
    @GET("tipo-material")
    suspend fun listarTiposMateriais(): Response<TiposMateriaisResponse>
}