package br.com.afya.consultacepkmp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.afya.consultacepkmp.ui.CepScreen
import br.com.afya.consultacepkmp.ui.theme.ConsultaCepKmpTheme
import br.com.afya.shared.platform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.i("MainActivity", "Hello from shared module: ${platform()}")
        setContent {
            ConsultaCepKmpTheme {
                CepScreen()
            }
        }
    }
}