package br.com.afya.cadastroalunos.ui.state

sealed interface ExclusaoTurmaUiState {
    data object Ocioso : ExclusaoTurmaUiState
    data object Excluindo : ExclusaoTurmaUiState
    data object Sucesso : ExclusaoTurmaUiState
    data class Erro(val mensagem: String) : ExclusaoTurmaUiState
}