package br.com.afya.cadastroalunos.ui.state

sealed interface ExclusaoDisciplinaUiState {
    data object Ocioso : ExclusaoDisciplinaUiState
    data object Excluindo : ExclusaoDisciplinaUiState
    data object Sucesso : ExclusaoDisciplinaUiState
    data class Erro(val mensagem: String) : ExclusaoDisciplinaUiState
}