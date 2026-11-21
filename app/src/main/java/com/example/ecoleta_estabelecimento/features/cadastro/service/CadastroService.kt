package com.example.ecoleta_estabelecimento.features.cadastro.service

import com.example.ecoleta_estabelecimento.features.cadastro.model.CadastroEstabelecimentoRequest
import com.example.ecoleta_estabelecimento.features.cadastro.model.CadastroEstabelecimentoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CadastroService {
    @POST("cadastro/estabelecimento")
    suspend fun cadastrarEstabelecimento(
        @Body cadastro: CadastroEstabelecimentoRequest
    ): Response<CadastroEstabelecimentoResponse>
}