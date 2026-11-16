package br.com.afya.shared.data.remote

import br.com.afya.shared.data.model.EnderecoResponse

interface ViaCepApi {
    suspend fun buscarCep(cep: String): EnderecoResponse
}