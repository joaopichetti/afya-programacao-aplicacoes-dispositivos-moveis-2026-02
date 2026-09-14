package br.com.afya.cadastroalunos.model

data class Aluno(
    val id: Int = 0,
    val nome: String = "",
    val idade: Int = 0,
    val mensalidade: Double = 0.0,
    val ativo: Boolean = true,
    val linguagens: List<String> = emptyList(),
    val dataDeCadastro: String? = null
)
