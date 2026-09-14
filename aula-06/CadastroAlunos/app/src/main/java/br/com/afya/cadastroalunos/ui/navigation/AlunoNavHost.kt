package br.com.afya.cadastroalunos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    }
}