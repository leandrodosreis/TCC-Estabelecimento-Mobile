package com.example.ecoleta_estabelecimento.auth

class TokenManual : TokenProvider {
    override fun getToken(): String? {
        // Token obtido pelo JWT (sem a tela de login é necessário inserir manualmente)
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9lc3RhYmVsZWNpbWVudG8iOjUsImlhdCI6MTc5NDU4OTU1NCwiZXhwIjoxNzk0NjI1MjAxfQ.RnRqYo8eqTXcjLxshygSy-UIgocssdOX-hQA_h75OVM"
    }
}