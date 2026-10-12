package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.TurmaDetalhes

sealed interface ListaTurmasUiState {
    data object Carregando : ListaTurmasUiState
    data class Sucesso(val turmas: List<TurmaDetalhes>) : ListaTurmasUiState
    data class Erro(val mensagem: String) : ListaTurmasUiState
}