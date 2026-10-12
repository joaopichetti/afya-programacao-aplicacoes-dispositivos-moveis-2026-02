package br.com.afya.cadastroalunos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.afya.cadastroalunos.ui.telas.DetalhesTurmaScreen
import br.com.afya.cadastroalunos.ui.telas.FormularioAlunoScreen
import br.com.afya.cadastroalunos.ui.telas.FormularioDisciplinaScreen
import br.com.afya.cadastroalunos.ui.telas.FormularioTurmaScreen
import br.com.afya.cadastroalunos.ui.telas.ListaAlunosScreen
import br.com.afya.cadastroalunos.ui.telas.ListaDisciplinasScreen
import br.com.afya.cadastroalunos.ui.telas.ListaTurmasScreen
import br.com.afya.cadastroalunos.ui.viewmodel.AlunoViewModel
import br.com.afya.cadastroalunos.ui.viewmodel.DisciplinaViewModel
import br.com.afya.cadastroalunos.ui.viewmodel.TurmaViewModel

@Composable
fun AppNavHost(
    alunoViewModel: AlunoViewModel = viewModel(),
    disciplinaViewModel: DisciplinaViewModel = viewModel(),
    turmaViewModel: TurmaViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // A BottomBar é exibida nas telas principais dos agrupamentos (listas)
    val isTopLevelRoute = currentDestination?.let { dest ->
        dest.hasRoute<AppRoute.AlunosLista>() ||
                dest.hasRoute<AppRoute.DisciplinasLista>() ||
                dest.hasRoute<AppRoute.TurmasLista>()
    } ?: true

    Scaffold(
        bottomBar = {
            if (isTopLevelRoute) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<AppRoute.AlunosLista>() == true,
                        onClick = {
                            navController.navigate(AppRoute.AlunosLista) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Alunos"
                            )
                        },
                        label = { Text("Alunos") }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<AppRoute.DisciplinasLista>() == true,
                        onClick = {
                            navController.navigate(AppRoute.DisciplinasLista) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = "Disciplinas"
                            )
                        },
                        label = { Text("Disciplinas") }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hasRoute<AppRoute.TurmasLista>() == true,
                        onClick = {
                            navController.navigate(AppRoute.TurmasLista) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Turmas"
                            )
                        },
                        label = { Text("Turmas") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.AlunosLista,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // ============================
            // Agrupamento: Alunos
            // ============================
            composable<AppRoute.AlunosLista> {
                ListaAlunosScreen(
                    listaUiState = alunoViewModel.listaUiState,
                    exclusaoUiState = alunoViewModel.exclusaoUiState,
                    ordenacaoAtual = alunoViewModel.ordenacaoAtual,
                    onOrdenacaoChange = {
                        alunoViewModel.alterarOrdenacao(it)
                    },
                    onAdicionarClick = {
                        navController.navigate(AppRoute.AlunoFormulario())
                    },
                    onEditarClick = { aluno ->
                        navController.navigate(AppRoute.AlunoFormulario(alunoId = aluno.id))
                    },
                    onExcluirClick = { aluno ->
                        alunoViewModel.excluir(aluno)
                    },
                    onTentarNovamente = {
                        alunoViewModel.observarAlunos()
                    },
                    onExclusaoMensagemMostrada = {
                        alunoViewModel.limparExclusaoUiState()
                    }
                )
            }

            composable<AppRoute.AlunoFormulario> { backStackEntry ->
                val args = backStackEntry.toRoute<AppRoute.AlunoFormulario>()
                val alunoId = args.alunoId

                LaunchedEffect(alunoId) {
                    if (alunoId == 0) {
                        alunoViewModel.prepararNovoAluno()
                    } else {
                        alunoViewModel.carregarAluno(alunoId)
                    }
                }

                FormularioAlunoScreen(
                    formularioUiState = alunoViewModel.formularioUiState,
                    salvarUiState = alunoViewModel.salvarUiState,
                    onSalvarClick = { aluno ->
                        alunoViewModel.salvar(aluno)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onTentarNovamente = {
                        if (alunoId == 0) {
                            alunoViewModel.prepararNovoAluno()
                        } else {
                            alunoViewModel.carregarAluno(alunoId)
                        }
                    },
                    onSalvoComSucesso = {
                        alunoViewModel.limparSalvarUiState()
                        alunoViewModel.observarAlunos()
                        navController.popBackStack()
                    },
                    onErroMostrado = {
                        alunoViewModel.limparSalvarUiState()
                    }
                )
            }

            // ============================
            // Agrupamento: Disciplinas
            // ============================
            composable<AppRoute.DisciplinasLista> {
                ListaDisciplinasScreen(
                    listaUiState = disciplinaViewModel.listaUiState,
                    exclusaoUiState = disciplinaViewModel.exclusaoUiState,
                    ordenacaoAtual = disciplinaViewModel.ordenacaoAtual,
                    onOrdenacaoChange = { disciplinaViewModel.alterarOrdenacao(it) },
                    onAdicionarClick = {
                        navController.navigate(AppRoute.DisciplinaFormulario())
                    },
                    onEditarClick = { disciplina ->
                        navController.navigate(AppRoute.DisciplinaFormulario(disciplinaId = disciplina.id))
                    },
                    onExcluirClick = { disciplina ->
                        disciplinaViewModel.excluir(disciplina)
                    },
                    onTentarNovamente = {
                        disciplinaViewModel.carregarDisciplinas()
                    },
                    onExclusaoMensagemMostrada = {
                        disciplinaViewModel.limparExclusaoUiState()
                    }
                )
            }

            composable<AppRoute.DisciplinaFormulario> { backStackEntry ->
                val args = backStackEntry.toRoute<AppRoute.DisciplinaFormulario>()
                val disciplinaId = args.disciplinaId

                LaunchedEffect(disciplinaId) {
                    if (disciplinaId == 0) {
                        disciplinaViewModel.prepararNovaDisciplina()
                    } else {
                        disciplinaViewModel.carregarDisciplina(disciplinaId)
                    }
                }

                FormularioDisciplinaScreen(
                    formularioUiState = disciplinaViewModel.formularioUiState,
                    salvarUiState = disciplinaViewModel.salvarUiState,
                    onSalvarClick = { disciplina ->
                        disciplinaViewModel.salvar(disciplina)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onTentarNovamente = {
                        if (disciplinaId == 0) {
                            disciplinaViewModel.prepararNovaDisciplina()
                        } else {
                            disciplinaViewModel.carregarDisciplina(disciplinaId)
                        }
                    },
                    onSalvoComSucesso = {
                        disciplinaViewModel.limparSalvarUiState()
                        disciplinaViewModel.carregarDisciplinas()
                        navController.popBackStack()
                    },
                    onErroMostrado = {
                        disciplinaViewModel.limparSalvarUiState()
                    }
                )
            }

            // ============================
            // Agrupamento: Turmas
            // ============================
            composable<AppRoute.TurmasLista> {
                ListaTurmasScreen(
                    listaUiState = turmaViewModel.listaUiState,
                    exclusaoUiState = turmaViewModel.exclusaoUiState,
                    ordenacaoAtual = turmaViewModel.ordenacaoAtual,
                    onOrdenacaoChange = { turmaViewModel.alterarOrdenacao(it) },
                    onAdicionarClick = {
                        navController.navigate(AppRoute.TurmaFormulario())
                    },
                    onDetalhesClick = { turma ->
                        navController.navigate(AppRoute.TurmaDetalhes(turmaId = turma.id))
                    },
                    onEditarClick = { turma ->
                        navController.navigate(AppRoute.TurmaFormulario(turmaId = turma.id))
                    },
                    onExcluirClick = { turma ->
                        turmaViewModel.excluir(turma)
                    },
                    onTentarNovamente = {
                        turmaViewModel.carregarTurmas()
                    },
                    onExclusaoMensagemMostrada = {
                        turmaViewModel.limparExclusaoUiState()
                    }
                )
            }

            composable<AppRoute.TurmaFormulario> { backStackEntry ->
                val args = backStackEntry.toRoute<AppRoute.TurmaFormulario>()
                val turmaId = args.turmaId

                LaunchedEffect(turmaId) {
                    if (turmaId == 0) {
                        turmaViewModel.prepararNovaTurma()
                    } else {
                        turmaViewModel.carregarTurma(turmaId)
                    }
                }

                FormularioTurmaScreen(
                    formularioUiState = turmaViewModel.formularioUiState,
                    salvarUiState = turmaViewModel.salvarUiState,
                    onSalvarClick = { turma ->
                        turmaViewModel.salvar(turma)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onTentarNovamente = {
                        if (turmaId == 0) {
                            turmaViewModel.prepararNovaTurma()
                        } else {
                            turmaViewModel.carregarTurma(turmaId)
                        }
                    },
                    onSalvoComSucesso = {
                        turmaViewModel.limparSalvarUiState()
                        turmaViewModel.carregarTurmas()
                        navController.popBackStack()
                    },
                    onErroMostrado = {
                        turmaViewModel.limparSalvarUiState()
                    }
                )
            }

            composable<AppRoute.TurmaDetalhes> { backStackEntry ->
                val args = backStackEntry.toRoute<AppRoute.TurmaDetalhes>()
                val turmaId = args.turmaId

                LaunchedEffect(turmaId) {
                    turmaViewModel.carregarDetalhesTurma(turmaId)
                }

                DetalhesTurmaScreen(
                    detalhesUiState = turmaViewModel.detalhesUiState,
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onEditarClick = { turma ->
                        navController.navigate(AppRoute.TurmaFormulario(turmaId = turma.id))
                    },
                    onAdicionarAlunoClick = { tId, aId ->
                        turmaViewModel.adicionarAlunoNaTurma(tId, aId)
                    },
                    onRemoverAlunoClick = { tId, aId ->
                        turmaViewModel.removerAlunoDaTurma(tId, aId)
                    },
                    onTentarNovamente = {
                        turmaViewModel.carregarDetalhesTurma(turmaId)
                    }
                )
            }
        }
    }
}