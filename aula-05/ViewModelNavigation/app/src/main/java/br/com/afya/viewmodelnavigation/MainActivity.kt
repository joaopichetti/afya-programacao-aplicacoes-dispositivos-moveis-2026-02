package br.com.afya.viewmodelnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.afya.viewmodelnavigation.ui.navigation.AppNavigation
import br.com.afya.viewmodelnavigation.ui.theme.ViewModelNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewModelNavigationTheme {
                AppNavigation()
            }
        }
    }
}