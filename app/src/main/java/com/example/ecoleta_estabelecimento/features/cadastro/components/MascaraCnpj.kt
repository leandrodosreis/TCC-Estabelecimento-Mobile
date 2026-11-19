package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MascaraCnpj : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text.take(14)

        val cnpjFormatado = buildString {
            digitos.forEachIndexed { indice, caractere ->
                when (indice) {
                    2, 5 -> append('.')
                    8 -> append('/')
                    12 -> append('-')
                }
                append(caractere)
            }
        }

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val posicao = offset.coerceIn(0, digitos.length)

                return (posicao +
                        (if (posicao > 2) 1 else 0) +
                        (if (posicao > 5) 1 else 0) +
                        (if (posicao > 8) 1 else 0) +
                        (if (posicao > 12) 1 else 0)
                        ).coerceAtMost(cnpjFormatado.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return cnpjFormatado
                    .take(offset.coerceIn(0, cnpjFormatado.length))
                    .count { it.isLetterOrDigit() }
                    .coerceAtMost(digitos.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(cnpjFormatado),
            offsetMapping = mapeamento
        )
    }
}
