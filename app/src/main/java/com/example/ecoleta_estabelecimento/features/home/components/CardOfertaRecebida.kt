package com.example.ecoleta_estabelecimento.features.home.components

import androidx.compose.runtime.Composable
import com.example.ecoleta_estabelecimento.components.CardOfertaEcoleta.CardOfertaEcoleta
import com.example.ecoleta_estabelecimento.features.home.model.OfertaResumo
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CardOfertaRecebida(
    oferta: OfertaResumo,
    onVerOferta: (Int) -> Unit = {}
) {
    // Formata o valor para o padrão monetário brasileiro
    val valorFormatado = NumberFormat
        .getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
        .format(oferta.valor)

    // Formata a quantidade sem zeros desnecessários
    val quantidadeFormatada = oferta.quantidadeKg
        .toString()
        .replace(',', '.')
        .toBigDecimalOrNull()
        ?.stripTrailingZeros()
        ?.toPlainString()
        ?.replace('.', ',')
        ?: oferta.quantidadeKg.toString()

    // Reutiliza o card da tela de Coletas
    CardOfertaEcoleta(
        nomeEmpresa = oferta.nomeColetor,
        material = oferta.material,
        quantidade = "$quantidadeFormatada kg",
        data = oferta.data,
        valor = valorFormatado,
        status = oferta.status,

        // Mantém o callback e o ID da oferta
        onVerMais = {
            onVerOferta(oferta.id)
        }
    )
}