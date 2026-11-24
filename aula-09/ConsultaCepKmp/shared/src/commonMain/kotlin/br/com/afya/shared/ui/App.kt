package br.com.afya.shared.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.afya.shared.ui.historico.HistoricoScreen
import br.com.afya.shared.ui.theme.ConsultaCepKmpTheme

@Composable
fun App() {
    ConsultaCepKmpTheme {
        val navController = rememberNavController()

        NavHost(navController= navController, startDestination = CepRoute) {
            composable<CepRoute> {
                CepScreen(
                    onHistoricoClick = {
                        navController.navigate(HistoricoRoute)
                    }
                )
            }
            composable<HistoricoRoute> {
                HistoricoScreen(
                    onVoltarClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}