package br.com.afya.cadastroalunos.model

enum class OrdenacaoTurma(val titulo: String) {
    NOME_ASC("Nome (A-Z)"),
    NOME_DESC("Nome (Z-A)"),
    DISCIPLINA_ASC("Disciplina (A-Z)"),
    DISCIPLINA_DESC("Disciplina (Z-A)"),
    ALUNOS_DESC("Mais alunos"),
    ALUNOS_ASC("Menos alunos")
}