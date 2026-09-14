package br.com.afya.cadastroalunos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.afya.cadastroalunos.ui.navigation.AlunoNavHost
import br.com.afya.cadastroalunos.ui.theme.CadastroAlunosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CadastroAlunosTheme {
                AlunoNavHost()
            }
        }
    }
}
