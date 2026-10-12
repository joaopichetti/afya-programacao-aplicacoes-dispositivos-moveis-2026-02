package br.com.afya.cadastroalunos.ui.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    // Agrupamento de Telas: Alunos
    @Serializable
    data object AlunosLista : AppRoute

    @Serializable
    data class AlunoFormulario(val alunoId: Int = 0) : AppRoute

    // Agrupamento de Telas: Disciplinas
    @Serializable
    data object DisciplinasLista : AppRoute

    @Serializable
    data class DisciplinaFormulario(val disciplinaId: Int = 0) : AppRoute

    // Agrupamento de Telas: Turmas
    @Serializable
    data object TurmasLista : AppRoute

    @Serializable
    data class TurmaFormulario(val turmaId: Int = 0) : AppRoute

    @Serializable
    data class TurmaDetalhes(val turmaId: Int) : AppRoute
}