package com.example.ecoleta_estabelecimento.features.perfil.service

import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilResponse
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilAtualizar
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilAtualizarApiResponse
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenha
import com.example.ecoleta_estabelecimento.features.perfil.model.PerfilSenhaResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.PATCH
import retrofit2.http.Body

interface PerfilService {

    // Busca os dados do perfil do estabelecimento autenticado
    @GET("estabelecimento/perfil")
    suspend fun buscarPerfil(): Response<PerfilResponse>


    // Atualiza os dados cadastrais e endereço do estabelecimento
    @PUT("estabelecimento/perfil")
    suspend fun atualizarPerfil(
        @Body dados: PerfilAtualizar
    ): Response<PerfilAtualizarApiResponse>


    // Altera a senha do estabelecimento
    @PATCH("estabelecimento/perfil/senha")
    suspend fun alterarSenha(
        @Body dados: PerfilSenha
    ): Response<PerfilSenhaResponse>

}