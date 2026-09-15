package br.com.afya.cadastroalunos.ui.state

sealed interface ExclusaoAlunoUiState {
    data object Ocioso : ExclusaoAlunoUiState
    data object Excluindo : ExclusaoAlunoUiState
    data object Sucesso : ExclusaoAlunoUiState
    data class Erro(val mensagem: String) : ExclusaoAlunoUiState
}
