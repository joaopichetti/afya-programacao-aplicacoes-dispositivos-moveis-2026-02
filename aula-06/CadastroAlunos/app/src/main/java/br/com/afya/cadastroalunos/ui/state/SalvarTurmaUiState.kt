package br.com.afya.cadastroalunos.ui.state

sealed interface SalvarTurmaUiState {
    data object Ocioso : SalvarTurmaUiState
    data object Salvando : SalvarTurmaUiState
    data object Sucesso : SalvarTurmaUiState
    data class Erro(val mensagem: String) : SalvarTurmaUiState
}