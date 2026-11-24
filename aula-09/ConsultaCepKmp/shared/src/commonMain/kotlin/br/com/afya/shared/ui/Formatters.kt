package br.com.afya.shared.ui

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

private val dataHoraFormat = LocalDateTime.Format {
    day()
    char('/')
    monthNumber()
    char('/')
    year()
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
}

internal fun formatarCep(cep: String): String {
    val digitsOnly = cep.filter { it.isDigit() }
    return if (digitsOnly.length == 8) {
        "${digitsOnly.substring(0, 5)}-${digitsOnly.substring(5)}"
    } else {
        cep
    }
}

// Converte o instante (UTC) para o fuso horário configurado no dispositivo
internal fun formatarDataHora(instant: Instant): String {
    val dataHoraLocal = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return dataHoraFormat.format(dataHoraLocal)
}