package br.com.afya.cadastroalunos.model

data class TurmaDetalhes(
    val turma: Turma,
    val disciplina: Disciplina?,
    val alunos: List<Aluno> = emptyList()
)
