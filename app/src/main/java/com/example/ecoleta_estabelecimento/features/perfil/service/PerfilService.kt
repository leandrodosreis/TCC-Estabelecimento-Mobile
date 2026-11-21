package com.example.ecoleta_estabelecimento.features.perfil.service

import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilAtualizar
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilAtualizarResponse
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilEstabelecimentoResponse
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenhaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface PerfilService {
    @GET("estabelecimento/perfil")
    suspend fun buscarPerfil(): Response<PerfilEstabelecimentoResponse>

    @PUT("estabelecimento/perfil")
    suspend fun atualizarPerfil(
        @Body dados: PerfilAtualizar
    ): Response<PerfilAtualizarResponse>

    @PATCH("estabelecimento/perfil/senha")
    suspend fun alterarSenha(
        @Body dados: PerfilSenha
    ): Response<PerfilSenhaResponse>
}