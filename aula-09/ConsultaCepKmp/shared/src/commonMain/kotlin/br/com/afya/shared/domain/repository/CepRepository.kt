package br.com.afya.shared.domain.repository

import br.com.afya.shared.domain.model.Endereco

interface CepRepository {
    suspend fun buscarCep(cep: String): Result<Endereco>

    @Throws(Exception::class)
    suspend fun buscarCepOrThrow(cep: String): Endereco =
        buscarCep(cep).getOrThrow()
}