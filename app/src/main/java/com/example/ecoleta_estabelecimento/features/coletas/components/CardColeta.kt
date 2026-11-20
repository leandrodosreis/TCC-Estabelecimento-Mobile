package com.example.ecoleta_estabelecimento.features.coletas.components

import androidx.compose.runtime.Composable
import com.example.ecoleta_estabelecimento.components.CardOfertaEcoleta.CardOfertaEcoleta
import com.example.ecoleta_estabelecimento.features.coletas.model.Coleta
import java.text.NumberFormat
import java.util.Locale

// Formatação da quantidade
private fun formatarQuantidadeColeta(
    quantidade: String
): String {
    val texto = quantidade.trim()

    if (texto.endsWith("kg", ignoreCase = true)) {
        return texto
    }

    val numero = texto.replace(',', '.')
        .toBigDecimalOrNull()
        ?: return texto

    return "${numero.stripTrailingZeros().toPlainString().replace('.', ',')} kg"
}

// Formatação do valor ofertado
private fun formatarValorColeta(
    valor: String
): String {
    val texto = valor.trim()

    if (texto.startsWith("R$")) {
        return texto
    }

    val numero = texto.replace(',', '.')
        .toBigDecimalOrNull()
        ?: return texto

    val formato = NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("pt-BR")
    )

    return formato.format(numero)
}

// Card para exibir as ofertas
@Composable
fun CardColeta(
    coleta: Coleta,
    onVerMais: () -> Unit
) {
    // Mantém a formatação original da quantidade
    val quantidadeFormatada = formatarQuantidadeColeta(coleta.quantidade)

    // Mantém a formatação original do valor
    val valorFormatado = formatarValorColeta(coleta.valorOfertado)

    // Reutiliza o componente visual compartilhado
    CardOfertaEcoleta(
        nomeEmpresa = coleta.nomeEmpresaColetora,
        material = coleta.material,
        quantidade = quantidadeFormatada,
        data = coleta.dataDisponivel,
        valor = valorFormatado,
        status = coleta.statusOferta,
        onVerMais = onVerMais
    )
}