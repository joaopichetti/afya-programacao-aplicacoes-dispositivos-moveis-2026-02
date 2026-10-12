package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.Disciplina

sealed interface FormularioDisciplinaUiState {
    data object Ocioso : FormularioDisciplinaUiState
    data object Carregando : FormularioDisciplinaUiState
    data class Sucesso(val disciplina: Disciplina) : FormularioDisciplinaUiState
    data class Erro(val mensagem: String) : FormularioDisciplinaUiState
}