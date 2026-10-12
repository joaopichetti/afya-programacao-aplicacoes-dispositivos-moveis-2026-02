package br.com.afya.cadastroalunos.model

enum class OrdenacaoDisciplina(val titulo: String) {
    NOME_ASC("Nome (A-Z)"),
    NOME_DESC("Nome (Z-A)"),
    CODIGO_ASC("Código (A-Z)"),
    CODIGO_DESC("Código (Z-A)"),
    CARGA_HORARIA_ASC("Menor carga horária"),
    CARGA_HORARIA_DESC("Maior carga horária")
}