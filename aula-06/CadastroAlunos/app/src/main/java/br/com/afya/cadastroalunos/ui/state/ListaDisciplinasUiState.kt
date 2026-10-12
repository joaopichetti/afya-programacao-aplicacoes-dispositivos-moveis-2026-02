package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.Disciplina

sealed interface ListaDisciplinasUiState {
    data object Carregando : ListaDisciplinasUiState
    data class Sucesso(val disciplinas: List<Disciplina>) : ListaDisciplinasUiState
    data class Erro(val mensagem: String) : ListaDisciplinasUiState
}