package com.example.ecoleta_estabelecimento.auth

class TokenManual : TokenProvider {
    override fun getToken(): String? {
        // Token obtido pelo JWT (sem a tela de login é necessário inserir manualmente)
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9lc3RhYmVsZWNpbWVudG8iOjUsImlhdCI6MTc5NDg3MzM1MSwiZXhwIjoxNzk0ODg0NDAwfQ.aoD3QKu7gOFLIifCStwAhfLc54-5SychcqqXiQrYmI0"
    }
}