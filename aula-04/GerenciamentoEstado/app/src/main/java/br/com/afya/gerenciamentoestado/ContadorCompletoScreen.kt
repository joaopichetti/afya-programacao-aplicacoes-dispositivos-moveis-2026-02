package br.com.afya.gerenciamentoestado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.afya.gerenciamentoestado.ui.theme.GerenciamentoEstadoTheme


// =========================================================================
// 1. COMPONENTES STATELESS (Puramente Visuais, Reutilizáveis e Testáveis)
// =========================================================================
@Composable
private fun ItemHistorico(
    registro: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "- $registro",
        fontSize = 14.sp,
        modifier = modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ItemHistoricoPreview() {
    GerenciamentoEstadoTheme {
        ItemHistorico(
            registro = "Incrementou o valor"
        )
    }
}

@Composable
private fun PainelContador(
    valorContador: Int,
    inputIntervalo: String,
    onIntervaloChange: (String) -> Unit,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onZerar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$valorContador",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = if (valorContador >= 50) Color(0xFFCE0058) else Color(0xFF005787)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = inputIntervalo,
                onValueChange = onIntervaloChange,
                label = {
                    Text("Valor do intervalo")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onDecrementar) {
                    Text("Decrementar")
                }
                Button(
                    onClick = onZerar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Zerar")
                }
                Button(onClick = onIncrementar) {
                    Text("Incrementar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PainelContadorPreview() {
    GerenciamentoEstadoTheme {
        PainelContador(
            valorContador = 5,
            inputIntervalo = "1",
            onIntervaloChange = {},
            onIncrementar = {},
            onDecrementar = {},
            onZerar = {}
        )
    }
}

// =========================================================================
// 2. COMPONENTE STATEFUL (Gerenciador de Estado / Arquitetura UDF)
// =========================================================================
@Composable
fun ContadorCompletoScreen(modifier: Modifier = Modifier) {
    var contador: Int by rememberSaveable { mutableStateOf(0) }
    var inputIntervalo: String by rememberSaveable { mutableStateOf("1") }
    val valorIntervalo = inputIntervalo.toIntOrNull() ?: 1

    val historico = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PainelContador(
            valorContador = contador,
            inputIntervalo = inputIntervalo,
            onIntervaloChange = { inputIntervalo = it },
            onIncrementar = {
                contador += valorIntervalo
                historico.add(0, "Somou $valorIntervalo (Novo valor: $contador)")
            },
            onDecrementar =  {
                if (contador - valorIntervalo >= 0) {
                    contador -= valorIntervalo
                    historico.add(0,
                        "Subtraiu $valorIntervalo (Novo valor: $contador)")
                } else {
                    contador = 0
                    historico.add(0, "Travado no limite mínimo: 0")
                }
            },
            onZerar = {
                contador = 0
                historico.add(0, "Contador zerado")
            }
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Histórico de Ações (mutableStateListOf):",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            historico.forEach { registro ->
                ItemHistorico(registro = registro)
            }
        }
    }
}