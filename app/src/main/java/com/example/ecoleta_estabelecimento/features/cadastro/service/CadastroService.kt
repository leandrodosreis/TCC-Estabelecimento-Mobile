package com.example.ecoleta_estabelecimento.features.cadastro.service

import com.example.ecoleta_estabelecimento.features.cadastro.model.CadastrarUsuario
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CadastroService {
    // Cadastra um novo usuário
    @POST("cadastro/estabelecimento")
    suspend fun cadastrarEstabelecimento(
        @Body usuario: CadastrarUsuario
    ): Response<ResiduoCadastroResponse>
}