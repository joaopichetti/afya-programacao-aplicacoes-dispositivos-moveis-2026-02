package br.com.afya.viewmodelnavigation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.afya.viewmodelnavigation.ui.screen.CadastroOrdemScreen
import br.com.afya.viewmodelnavigation.ui.screen.ListaOrdensScreen
import br.com.afya.viewmodelnavigation.ui.viewmodel.OrdemServicoViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: OrdemServicoViewModel = viewModel()
) {
    // Coleta com proteção de ciclo de vida (suspende em segundo plano)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {
        // Rota principal
        composable("lista") {
            ListaOrdensScreen(
                uiState = uiState,
                onNovaOrdemClick = {
                    navController.navigate("cadastro")
                },
                onItemClick = { idOrdem ->
                    navController.navigate("detalhes/$idOrdem")
                }
            )
        }
        // Rota de Cadastro
        composable("cadastro") {
            CadastroOrdemScreen(
                uiState = uiState,
                onClienteChange = viewModel::onClienteChanged,
                onDescricaoChange = viewModel::onDescricaoChanged,
                onUrgenteChange = viewModel::onUrgenteChanged,
                onSalvarClick = {
                    viewModel.salvarOrdem(
                        onFinalizado = {
                            navController.popBackStack()
                        }
                    )
                },
                onVoltarClick = {
//                    navController.popBackStack()
                    navController.navigate("lista")
                }
            )
        }
        // Rota de Detalhes com Parâmetro Tipado
        composable(
            route = "detalhes/{ordemId}",
            arguments = listOf(
                navArgument("ordemId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("ordemId") ?: 0
            val ordem = uiState.ordens.find { it.id == id }
            // TODO - Implementar tela de detalhes exibindo a OS
            // encontrada e botão de retorno
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tela não implementada ainda...")
            }
        }
    }
}