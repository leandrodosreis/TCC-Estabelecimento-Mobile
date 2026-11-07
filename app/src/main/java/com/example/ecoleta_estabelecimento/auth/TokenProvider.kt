package com.example.ecoleta_estabelecimento.auth

// Cria uma interface que define uma regra;
    // Qualquer classe responsável por fornecer o JWT deverá possuir uma função que é essa de getToken
interface TokenProvider {
    fun getToken(): String?
}