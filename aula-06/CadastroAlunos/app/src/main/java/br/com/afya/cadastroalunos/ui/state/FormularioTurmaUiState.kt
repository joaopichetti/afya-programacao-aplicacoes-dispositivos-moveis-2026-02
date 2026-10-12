package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.model.Disciplina
import br.com.afya.cadastroalunos.model.Turma

sealed interface FormularioTurmaUiState {
    data object Ocioso : FormularioTurmaUiState
    data object Carregando : FormularioTurmaUiState
    data class Sucesso(
        val turma: Turma?,
        val disciplinas: List<Disciplina>,
        val alunos: List<Aluno>
    ) : FormularioTurmaUiState
    data class Erro(val mensagem: String) : FormularioTurmaUiState
}