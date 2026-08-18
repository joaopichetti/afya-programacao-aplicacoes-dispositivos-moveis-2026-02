package br.com.afya.composebasico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.afya.composebasico.ui.theme.ComposeBasicoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeBasicoTheme {
                CartaoAluno(
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartaoAluno(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = "Acadêmico Afya ADS"
        )
        Text(
            text = "4o Período - Dispositivos Móveis"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartaoAlunoPreview() {
    ComposeBasicoTheme {
        CartaoAluno(modifier = Modifier.padding(top = 10.dp))
    }
}