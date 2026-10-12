package br.com.afya.cadastroalunos.ui.state

import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.model.TurmaDetalhes

sealed interface DetalhesTurmaUiState {
    data object Carregando : DetalhesTurmaUiState
    data class Sucesso(
        val turmaDetalhes: TurmaDetalhes,
        val alunosDisponiveis: List<Aluno>
    ) : DetalhesTurmaUiState
    data class Erro(val mensagem: String) : DetalhesTurmaUiState
}