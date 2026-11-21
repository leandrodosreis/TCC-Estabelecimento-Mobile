package com.example.ecoleta_estabelecimento.features.cadastro.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// Aplica a máscara de CEP sem alterar o valor armazenado
class MascaraCep : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Limita o CEP a 8 dígitos
        val digitos = text.text.filter { it.isDigit() }.take(8)

        // Formata o CEP
        val cepFormatado = if (digitos.length > 5) {
            digitos.substring(0, 5) + "-" + digitos.substring(5)
        } else {
            digitos
        }

        // Controla a posição do cursor após a aplicação da máscara
        val mapeamento = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val posicao = offset.coerceIn(0, digitos.length)

                return if (digitos.length > 5 && posicao >= 5) {
                    posicao + 1
                } else {
                    posicao
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return cepFormatado
                    .take(offset.coerceIn(0, cepFormatado.length))
                    .count { it.isDigit() }
                    .coerceAtMost(digitos.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(cepFormatado),
            offsetMapping = mapeamento
        )
    }
}