package com.example.ecoleta_estabelecimento.features.residuos.service

import com.example.ecoleta_estabelecimento.features.residuos.model.DetalhesColetaResponse
import com.example.ecoleta_estabelecimento.features.residuos.model.MeusResiduosResponse
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroResponse
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCancelarResponse
import com.example.ecoleta_estabelecimento.features.residuos.model.TiposMateriaisResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ResiduoService {
    // Cadastra um novo resíduo
    @POST("estabelecimento/residuos")
    suspend fun cadastrarResiduo(
        @Body residuo: ResiduoCadastroRequest
    ): Response<ResiduoCadastroResponse>

    // Busca os resíduos do estabelecimento autenticado
    @GET("estabelecimento/meus-residuos")
    suspend fun listarMeusResiduos(): Response<MeusResiduosResponse>

    // Busca os detalhes da coleta de um resíduo específico
    @GET("estabelecimento/meus-residuos/{id_residuo}")
    suspend fun buscarDetalhesColeta(
        @Path("id_residuo") idResiduo: Int
    ): Response<DetalhesColetaResponse>

    // Busca todos os tipos de materiais cadastrados no sistema
    @GET("tipo-material")
    suspend fun listarTiposMateriais(): Response<TiposMateriaisResponse>

    // Cancela um resíduo do estabelecimento autenticado
    @PATCH("estabelecimento/residuos/{id_residuo}/cancelar")
    suspend fun cancelarResiduo(
        @Path("id_residuo") idResiduo: Int
    ): Response<ResiduoCancelarResponse>

    // Atualiza os dados de um resíduo do estabelecimento autenticado
    @PUT("estabelecimento/residuos/{id_residuo}")
    suspend fun editarResiduo(
        @Path("id_residuo") idResiduo: Int,
        @Body residuo: ResiduoCadastroRequest
    ): Response<ResiduoCadastroResponse>
}