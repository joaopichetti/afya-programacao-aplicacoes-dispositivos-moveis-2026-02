package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.Aluno

sealed interface ListaAlunosUiState {
    data object Carregando : ListaAlunosUiState
    data class Sucesso(val alunos: List<Aluno>) : ListaAlunosUiState
    data class Erro(val mensagem: String) : ListaAlunosUiState
}