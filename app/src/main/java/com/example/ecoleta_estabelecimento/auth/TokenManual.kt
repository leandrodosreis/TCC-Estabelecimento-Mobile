package com.example.ecoleta_estabelecimento.auth

class TokenManual : TokenProvider {
    override fun getToken(): String? {
        // Token obtido pelo JWT (sem a tela de login é necessário inserir manualmente)
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9lc3RhYmVsZWNpbWVudG8iOjYsImlhdCI6MTc5NDkzMzk0NiwiZXhwIjoxNzk0OTcwODAwfQ.imBaCayoMaaFqiEpv7vIGNMFJldG6yca3TR3T6ZWhn4"
    }
}