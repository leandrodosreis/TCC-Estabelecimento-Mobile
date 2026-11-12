package com.example.ecoleta_estabelecimento.features.telaCadastro.service

import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroRequest
import com.example.ecoleta_estabelecimento.features.residuos.model.ResiduoCadastroResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CadastroService {
    // Cadastro de usuário

    @POST()
    suspend fun cadastrarResiduo(
        @Body residuo: ResiduoCadastroRequest
    ): Response<ResiduoCadastroResponse>
}