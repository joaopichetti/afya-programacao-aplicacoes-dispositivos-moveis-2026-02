package br.com.afya.gerenciamentoestado

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.afya.gerenciamentoestado.ui.theme.GerenciamentoEstadoTheme

private val TAG = "GerenciamentoEstado"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GerenciamentoEstadoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Contador(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Contador(modifier: Modifier = Modifier) {
    var contador = 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Valor: $contador",
            fontSize = 28.sp
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                contador++
                Log.d(TAG, "Valor na memória: $contador")
            }
        ) {
            Text("Incrementar")
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun ContadorPreview() {
    GerenciamentoEstadoTheme {
        Contador()
    }
}