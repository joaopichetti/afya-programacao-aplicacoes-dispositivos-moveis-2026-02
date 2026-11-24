package br.com.afya.shared.data.repository

import br.com.afya.shared.data.remote.ViaCepApi
import br.com.afya.shared.data.remote.ViaCepApiImpl
import br.com.afya.shared.domain.model.Endereco
import br.com.afya.shared.domain.repository.CepRepository
import br.com.afya.shared.domain.repository.HistoricoRepository

class CepRepositoryImpl(
    private val api: ViaCepApi,
    private val historicoRepository: HistoricoRepository
) : CepRepository {

    constructor() : this(ViaCepApiImpl(), HistoricoRepositoryImpl())

    override suspend fun buscarCep(cep: String): Result<Endereco> {
        return runCatching {
            val response = api.buscarCep(cep)
            val encontrado = !response.erro.equals("true", ignoreCase = true)
            val endereco = if (encontrado) response.toDomain() else null

            // Toda resposta da API é registrada no histórico, inclusive CEP não encontrado
            historicoRepository.salvarConsulta(
                cepConsultado = cep,
                endereco = endereco
            )

            endereco ?: throw NoSuchElementException("CEP não encontrado")
        }
    }
}