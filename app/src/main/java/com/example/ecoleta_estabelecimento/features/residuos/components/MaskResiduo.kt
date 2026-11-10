package com.example.ecoleta_estabelecimento.features.residuos.components

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

object MaskResiduo {
    // Formata a data enquanto o usuário digita:
    // Exemplo: 5 -> 05/ | 25122026 -> 25/12/2026
    fun formatarData(valor: String): String? {
        // Mantém somente números e limita a data a oito dígitos
        val numeros = valor.filter { it.isDigit() }.take(8)

        if (numeros.isEmpty()) return ""

        // Se o primeiro número for maior que 3, ele precisa de um zero à esquerda
        val digitos = if (numeros.length == 1 && numeros[0] in '4'..'9') {
            "0${numeros}"
        } else {
            numeros
        }

        // Aguarda o segundo dígito quando o dia começa com 0, 1, 2 ou 3
        if (digitos.length == 1) return digitos

        // Obtém os dois primeiros números, que representam o dia
        val diaDigitado = digitos.take(2).toIntOrNull()

        // Impede dias maiores que 31 ou iguais a zero, retornando null para avisar que a digitação deve ser rejeitada
        if (diaDigitado != null && diaDigitado !in 1..31) {
            return null
        }

        // Depois de dois dígitos, adiciona a barra do dia
        if (digitos.length == 2) return "${digitos}/"

        // Separa o dia dos números restantes
        val dia = digitos.take(2)
        val restante = digitos.drop(2)

        // Se o usuário ainda não digitou o mês, mantém apenas o dia
        if (restante.isEmpty()) return "$dia/"

        // Quando o primeiro número do mês está entre 2 e 9, adiciona automaticamente o zero à esquerda
        // Se for 0 ou 1, espera o próximo número
        val numerosMes = if (
            restante.length == 1 &&
            restante[0] in '2'..'9'
        ) {
            "0$restante"
        } else {
            restante
        }

        // Se ainda existe apenas um número, espera o segundo
        if (numerosMes.length == 1) return "$dia/$numerosMes"

        // Obtém os dois números que representam o mês
        val mes = numerosMes.take(2)
        val mesDigitado = mes.toIntOrNull()

        // Rejeita meses menores que 1 ou maiores que 12
        if (mesDigitado == null || mesDigitado !in 1..12) {
            return null
        }

        // Depois de completar o mês, adiciona a barra do ano
        if (numerosMes.length == 2) return "$dia/$mes/"

        // Adiciona o ano com até quatro dígitos
        val ano = numerosMes.drop(2).take(4)

        return "$dia/$mes/$ano"

    }

    // Verifica se a data realmente existe no calendário
    // Também considera anos bissextos, como 29/02/2028
    fun dataValida(valor: String): Boolean {
        return try {
            val formato = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT)

            LocalDate.parse(valor, formato)
            true

        } catch (_: DateTimeParseException) {
            false
        }
    }

    // Verifica se as horas estão entre 00 e 23
    // e os minutos estão entre 00 e 59
    fun horarioValido(valor: String): Boolean {
        if (!Regex("""\d{2}:\d{2}""").matches(valor)) {
            return false
        }

        val partes = valor.split(":")
        val horas = partes[0].toInt()
        val minutos = partes[1].toInt()

        return horas in 0..23 && minutos in 0..59
    }

    // Verifica se o horário final é posterior ao inicial
    fun intervaloHorarioValido(
        horarioInicial: String,
        horarioFinal: String
    ): Boolean {
        // Verifica primeiro se os dois horários são válidos
        if (!horarioValido(horarioInicial) || !horarioValido(horarioFinal)
        ) {
            return false
        }

        // Converte os textos em horários reais
        val inicio = LocalTime.parse(horarioInicial)
        val fim = LocalTime.parse(horarioFinal)

        // O horário final precisa ser maior que o inicial
        return fim.isAfter(inicio)
    }

    // Verifica se o texto digitado tem um formato permitido
    // Durante a digitação, aceita valores incompletos como "30," ou "30,5".
    fun quantidadeDigitadaValida(valor: String): Boolean {
        // Permite apagar todo o conteúdo do campo.
        if (valor.isEmpty()) return true

        // Aceita até quatro dígitos antes da vírgula e até dois dígitos depois dela.
        if (!Regex("""\d{1,4}([,.]\d{0,2})?""").matches(valor)) {
            return false
        }

        // Normaliza a vírgula para validar o valor numérico.
        val numero = valor.replace(',', '.').toBigDecimalOrNull()
            ?: return false

        // Limita o valor máximo a 9999,99.
        return numero <= BigDecimal("9999.99")
    }

    // Formata a quantidade quando o usuário sai do campo
    // Exemplo: "30" -> "30,00"
    fun formatarQuantidade(valor: String): String {
        // Mantém o campo vazio se nada foi digitado
        if (valor.isBlank()) return ""

        // Converte a vírgula para ponto antes de processar
        val numero = valor.replace(',', '.').toBigDecimalOrNull()
            ?: return valor

        // Garante exatamente duas casas decimais.
        return numero
            .setScale(2, RoundingMode.DOWN)
            .toPlainString()
            .replace('.', ',')
    }

    // Converte a quantidade visual para o formato do backend
    // Exemplo: 20,50 -> 20.50
    fun quantidadeParaBackend(valor: String): String {
        return valor.replace(',', '.')
    }
}