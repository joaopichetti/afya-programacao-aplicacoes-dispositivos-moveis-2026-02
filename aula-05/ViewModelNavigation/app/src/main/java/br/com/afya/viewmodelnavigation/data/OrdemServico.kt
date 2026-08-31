package br.com.afya.viewmodelnavigation.data

// Entidade de Domínio Puro
data class OrdemServico(
    val id: Int,
    val cliente: String,
    val descricao: String,
    val urgente: Boolean
)
