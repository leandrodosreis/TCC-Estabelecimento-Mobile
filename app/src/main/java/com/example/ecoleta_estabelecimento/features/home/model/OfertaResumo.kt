package com.example.ecoleta_estabelecimento.features.home.model

data class OfertaResumo (
    val id: Int,
    val nomeColetor: String,
    val material: String,
    val quantidadeKg: String,
    val data: String,
    val valor: Double,
    val status: String
)