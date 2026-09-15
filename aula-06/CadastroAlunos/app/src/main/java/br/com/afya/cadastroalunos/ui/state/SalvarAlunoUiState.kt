package br.com.afya.cadastroalunos.ui.state

sealed interface SalvarAlunoUiState {
    data object Ocioso : SalvarAlunoUiState
    data object Salvando : SalvarAlunoUiState
    data object Sucesso : SalvarAlunoUiState
    data class Erro(val mensagem: String) : SalvarAlunoUiState
}