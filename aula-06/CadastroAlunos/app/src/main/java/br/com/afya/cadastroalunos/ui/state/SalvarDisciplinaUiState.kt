package br.com.afya.cadastroalunos.ui.state

sealed interface SalvarDisciplinaUiState {
    data object Ocioso : SalvarDisciplinaUiState
    data object Salvando : SalvarDisciplinaUiState
    data object Sucesso : SalvarDisciplinaUiState
    data class Erro(val mensagem: String) : SalvarDisciplinaUiState
}