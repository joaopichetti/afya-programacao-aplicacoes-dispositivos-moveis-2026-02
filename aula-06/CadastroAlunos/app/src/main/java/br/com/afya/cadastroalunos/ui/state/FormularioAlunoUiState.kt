package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.Aluno

sealed interface FormularioAlunoUiState {
    data object Ocioso : FormularioAlunoUiState
    data object Carregando : FormularioAlunoUiState
    data class Sucesso(val aluno: Aluno) : FormularioAlunoUiState
    data class Erro(val mensagem: String) : FormularioAlunoUiState
}