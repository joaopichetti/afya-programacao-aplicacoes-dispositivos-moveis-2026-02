package br.com.afya.cadastroalunos.model

enum class OrdenacaoAluno(val titulo: String) {
    NOME_ASC("Nome (A-Z)"),
    NOME_DESC("Nome (Z-A)"),
    MENSALIDADE_ASC("Menor mensalidade"),
    MENSALIDADE_DESC("Maior mensalidade"),
    IDADE_ASC("Menor idade"),
    IDADE_DESC("Maior idade")
}