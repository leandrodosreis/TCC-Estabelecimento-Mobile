package com.example.ecoleta_estabelecimento.auth

class TokenManual : TokenProvider {
    override fun getToken(): String? {
        // Token obtido pelo JWT (sem a tela de login é necessário inserir manualmente)
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9lc3RhYmVsZWNpbWVudG8iOjUsImlhdCI6MTc5NDQ5OTY4MywiZXhwIjoxNzk0NTM4ODAwfQ.ToVzLcnCzI4DSKFAZetbxwjisY6z2yohQzTkuHnG33A"
    }
}