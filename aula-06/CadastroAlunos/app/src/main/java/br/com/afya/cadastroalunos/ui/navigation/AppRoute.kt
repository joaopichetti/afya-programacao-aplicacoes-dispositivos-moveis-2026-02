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
}