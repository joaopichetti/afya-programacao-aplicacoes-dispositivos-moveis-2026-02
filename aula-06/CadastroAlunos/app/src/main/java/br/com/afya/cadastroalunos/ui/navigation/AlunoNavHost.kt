package br.com.afya.cadastroalunos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.afya.cadastroalunos.ui.telas.FormularioAlunoScreen
import br.com.afya.cadastroalunos.ui.telas.ListaAlunosScreen
import br.com.afya.cadastroalunos.ui.viewmodel.AlunoViewModel

@Composable
fun AlunoNavHost(viewModel: AlunoViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaAlunosScreen(
                listaUiState = viewModel.listaUiState,
                exclusaoUiState = viewModel.exclusaoUiState,
                onAdicionarClick = {
                    navController.navigate("formulario")
                },
                onEditarClick = { aluno ->
                    navController.navigate("formulario/${aluno.id}")
                },
                onExcluirClick = { aluno ->
                    viewModel.excluir(aluno)
                },
                onTentarNovamente = {
                    viewModel.carregarAlunos()
                },
                onExclusaoMensagemMostrada = {
                    viewModel.limparExclusaoUiState()
                }
            )
        }

        composable("formulario") {
            LaunchedEffect(Unit) {
                viewModel.prepararNovoAluno()
            }

            FormularioAlunoScreen(
                formularioUiState = viewModel.formularioUiState,
                salvarUiState = viewModel.salvarUiState,
                onSalvarClick = { aluno ->
                    viewModel.salvar(aluno)
                },
                onVoltarClick = {
                    navController.popBackStack()
                },
                onTentarNovamente = { },
                onSalvoComSucesso = {
                    viewModel.carregarAlunos()
                    navController.popBackStack()
                },
                onErroMostrado = {
                    viewModel.limparSalvarUiState()
                }
            )
        }

        composable(
            route = "formulario/{alunoId}",
            arguments = listOf(navArgument("alunoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val alunoId = backStackEntry.arguments?.getInt("alunoId") ?: 0

            LaunchedEffect(alunoId) {
                viewModel.carregarAluno(alunoId)
            }

            FormularioAlunoScreen(
                formularioUiState = viewModel.formularioUiState,
                salvarUiState = viewModel.salvarUiState,
                onSalvarClick = { aluno ->
                    viewModel.salvar(aluno)
                },
                onVoltarClick = {
                    navController.popBackStack()
                },
                onTentarNovamente = {
                    viewModel.carregarAluno(alunoId)
                },
                onSalvoComSucesso = {
                    viewModel.carregarAlunos()
                    navController.popBackStack()
                },
                onErroMostrado = {
                    viewModel.limparSalvarUiState()
                }
            )
        }
    }
}