package br.com.afya.cadastroalunos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaAlunosScreen(
                alunos = viewModel.alunos,
                onAdicionarClick = {
                    navController.navigate("formulario")
                },
                onEditarClick = { aluno ->
                    navController.navigate("formulario/${aluno.id}")
                },
                onExcluirClick = { aluno ->
                    viewModel.excluir(aluno)
                }
            )
        }

        composable("formulario") {
            FormularioAlunoScreen(
                aluno = null,
                onSalvarClick = { aluno ->
                    viewModel.inserir(aluno)
                    navController.popBackStack()
                },
                onVoltarClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "formulario/{alunoId}",
            arguments = listOf(navArgument("alunoId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val alunoId = backStackEntry.arguments?.getInt("alunoId") ?: 0
            val aluno = viewModel.buscarPorId(alunoId)

            FormularioAlunoScreen(
                aluno = aluno,
                onSalvarClick = { alunoAtualizado ->
                    viewModel.atualizar(alunoAtualizado)
                    navController.popBackStack()
                },
                onVoltarClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}