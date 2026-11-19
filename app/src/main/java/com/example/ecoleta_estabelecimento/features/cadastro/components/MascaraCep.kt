package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MascaraCep : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text.take(8)

        val cepFormatado = buildString {
            digitos.forEachIndexed { indice, caractere ->
                if (indice == 5) append('-')
                append(caractere)
            }
        }

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val posicao = offset.coerceIn(0, digitos.length)
                return (posicao + if (posicao > 5) 1 else 0)
                    .coerceAtMost(cepFormatado.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return cepFormatado
                    .take(offset.coerceIn(0, cepFormatado.length))
                    .count { it.isLetterOrDigit() }
                    .coerceAtMost(digitos.length)
            }
        }

        return TransformedText(AnnotatedString(cepFormatado), mapeamento)
    }
}