package br.com.afya.shared.domain.model

import kotlin.time.Instant

// Item do histórico. "endereco" é nulo quando o CEP não foi encontrado pela API.
data class Consulta(
    val id: Long,
    val cepConsultado: String,
    val endereco: Endereco?,
    val dataConsulta: Instant
)
