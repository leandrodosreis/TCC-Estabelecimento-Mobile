package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MascaraTelefone : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text.take(11)

        val formatado = StringBuilder()
        // posicoes[i] = onde o dígito i ficou no texto formatado
        val posicoes = IntArray(digitos.length)

        digitos.forEachIndexed { indice, caractere ->
            when (indice) {
                0 -> formatado.append('(')
                2 -> formatado.append(") ")
                7 -> formatado.append('-')
            }
            posicoes[indice] = formatado.length
            formatado.append(caractere)
        }

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val posicao = offset.coerceIn(0, digitos.length)
                return when {
                    digitos.isEmpty() -> 0
                    posicao == 0 -> 1 // depois do "("
                    else -> posicoes[posicao - 1] + 1
                }.coerceAtMost(formatado.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return formatado
                    .take(offset.coerceIn(0, formatado.length))
                    .count { it.isDigit() }
                    .coerceAtMost(digitos.length)
            }
        }

        return TransformedText(AnnotatedString(formatado.toString()), mapeamento)
    }

}