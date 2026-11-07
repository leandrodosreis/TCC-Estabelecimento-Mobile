package com.example.ecoleta_estabelecimento.retrofit

import com.example.ecoleta_estabelecimento.auth.TokenManual
import com.example.ecoleta_estabelecimento.auth.TokenProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cria uma única instância compartilhada da configuração Retrofit
object RetrofitFactory {
    // URLs para os ambientes de teste
    private const val URL_EMULADOR = "http://10.0.2.2:8000/v1/ecoleta/" // (10.0.2.2) é o endereço especial do emulador Android (IP FIXO)

    private const val URL_CELULAR = "http://192.168.0.000:8000/v1/ecoleta/" // IP DO COMPUTADOR LOCAL

    // Altere aqui quando trocar o dispositivo de teste
    private const val BASE_URL = URL_EMULADOR

    // Responsável por fornecer o JWT
    private val tokenProvider: TokenProvider = TokenManual()

    // Configuração do cliente HTTP, criando um construtor de objetos, configurando seu comportamento
    private val okHttpClient = OkHttpClient.Builder()
        //Intercepta cada requisição antes de enviá-la e acrescenta automaticamente o cabeçalho com o token
        .addInterceptor { chain ->
            // Pega o token do getToken
            val token = tokenProvider.getToken()
            // Pega essa requisição que já existe e permite criar uma versão modificada dela
            val request = chain.request().newBuilder()
                .apply {
                    if (!token.isNullOrBlank()) {
                        header(
                            "Authorization",
                            "Bearer $token"
                        )
                    }
                }
                // Finaliza a configuração e cria o objeto
                .build()
            // Configura o JWT
            chain.proceed(request)
        }
        .build()

    // Instância principal do Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Disponibiliza os serviços HTTP
    fun <T> criarServico(service: Class<T>): T {
        return retrofit.create(service)
    }
}