package com.example.ecoleta_estabelecimento.features.login.service

import com.example.ecoleta_estabelecimento.features.login.model.LoginRequest
import com.example.ecoleta_estabelecimento.features.login.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Define as operações HTTP relacionadas à autenticação do estabelecimento
interface LoginService {
    @POST("login/estabelecimento")
    suspend fun realizarLogin(
        @Body dados: LoginRequest
    ): Response<LoginResponse>
}