package com.example.ecoleta_estabelecimento.auth

// Mantém o JWT em memória enquanto o aplicativo está ativo;
// O Retrofit consulta o token por meio de getToken(),
    // O armazenamento persistente fica no SessionManager
object TokenSessao : TokenProvider {
    // permite que o interceptor do OkHttp, enxergue o valor atualizado
    @Volatile
    private var token: String? = null

    override fun getToken(): String? {
        return token
    }

    fun salvarToken(novoToken: String) {
        token = novoToken
    }

    fun limparToken() {
        token = null
    }
}