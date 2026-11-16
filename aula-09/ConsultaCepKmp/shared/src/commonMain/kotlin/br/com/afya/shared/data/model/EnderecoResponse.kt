package br.com.afya.shared.data.model

import br.com.afya.shared.domain.model.Endereco
import kotlinx.serialization.Serializable

@Serializable
data class EnderecoResponse(
    val cep: String? = null,
    val logradouro: String? = null,
    val bairro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val erro: String? = null
) {
    fun toDomain(): Endereco {
        return Endereco(
            cep = cep.orEmpty(),
            logradouro = logradouro.orEmpty(),
            bairro = bairro.orEmpty(),
            cidade = localidade.orEmpty(),
            uf = uf.orEmpty()
        )
    }
}