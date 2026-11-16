package br.com.afya.shared.data.repository

import br.com.afya.shared.data.remote.ViaCepApi
import br.com.afya.shared.data.remote.ViaCepApiImpl
import br.com.afya.shared.domain.model.Endereco
import br.com.afya.shared.domain.repository.CepRepository

class CepRepositoryImpl(
    private val api: ViaCepApi
) : CepRepository {

    constructor() : this(ViaCepApiImpl())

    override suspend fun buscarCep(cep: String): Result<Endereco> {
        return runCatching {
            val response = api.buscarCep(cep)
            if (response.erro.equals("true", ignoreCase = true)) {
                throw NoSuchElementException("CEP não encontrado.")
            }
            response.toDomain()
        }
    }
}