package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// Aplica a máscara de telefone sem alterar o valor armazenado
class MascaraTelefone : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Limita o telefone a 11 dígitos
        val digitos = text.text.filter { it.isDigit() }.take(11)

        // Define a posição do traço conforme a quantidade de dígitos
        val indiceTraco = if (digitos.length == 11) 7 else 6

        // Formata o telefone
        val formatado = buildString {
            for (indice in 0..digitos.length) {

                when (indice) {
                    0 -> if (digitos.isNotEmpty()) append("(")
                    2 -> if (digitos.length >= 2) append(") ")
                    indiceTraco -> if (digitos.length > indiceTraco) append("-")
                }

                if (indice < digitos.length) {
                    append(digitos[indice])
                }
            }
        }

        // Registra a posição de cada dígito no texto formatado
        val posicoes = IntArray(digitos.length)

        var posicaoTexto = 0

        for (indice in 0..digitos.length) {

            when (indice) {
                0 -> if (digitos.isNotEmpty()) posicaoTexto++
                2 -> if (digitos.length >= 2) posicaoTexto += 2
                indiceTraco -> if (digitos.length > indiceTraco) posicaoTexto++
            }

            if (indice < digitos.length) {
                posicoes[indice] = posicaoTexto
                posicaoTexto++
            }
        }

        // Controla a posição do cursor após a aplicação da máscara
        val mapeamento = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val posicao = offset.coerceIn(0, digitos.length)

                return if (posicao < digitos.length) {
                    posicoes[posicao]
                } else {
                    formatado.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return formatado
                    .take(offset.coerceIn(0, formatado.length))
                    .count { it.isDigit() }
                    .coerceAtMost(digitos.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(formatado),
            offsetMapping = mapeamento
        )
    }
}