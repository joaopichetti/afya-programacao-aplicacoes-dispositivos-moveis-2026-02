package br.com.afya.composebasico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Avatar do Aluno",
            modifier = Modifier
                .size(56.dp),
            tint = Color(0xFFCE0058)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Acadêmico Afya ADS"
            )
            Text(
                text = "4o Período - Dispositivos Móveis"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartaoAlunoPreview() {
    ComposeBasicoTheme {
        CartaoAluno(modifier = Modifier.padding(top = 10.dp))
    }
}