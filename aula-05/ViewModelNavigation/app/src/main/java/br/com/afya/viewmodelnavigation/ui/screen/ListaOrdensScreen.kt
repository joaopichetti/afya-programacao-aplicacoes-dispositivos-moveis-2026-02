package br.com.afya.viewmodelnavigation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.afya.viewmodelnavigation.data.OrdemServico
import br.com.afya.viewmodelnavigation.ui.state.OrdemServicoUiState
import br.com.afya.viewmodelnavigation.ui.theme.ViewModelNavigationTheme

// TELA 1: LISTAGEM PURAMENTE STATELESS (Ideal para @Preview e Testes de UI)
@Composable
fun ListaOrdensScreen(
    uiState: OrdemServicoUiState,
    onNovaOrdemClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovaOrdemClick,
                containerColor = Color(0xFFCE0058)
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 22.sp
                )
            }
        }
    ) { padding ->
        if (uiState.ordens.isEmpty()) {
            ListaVazia(modifier = Modifier.padding(padding))
        } else {
            ListaPreenchida(
                uiState = uiState,
                onItemClick = onItemClick,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text("Ordens de Serviço")
        }
    )
}

@Composable
fun ListaVazia(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhuma ordem cadastrada.",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Toque em '+' para criar.",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun ListaPreenchida(
    uiState: OrdemServicoUiState,
    onItemClick: (Int) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(uiState.ordens) { ordem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { onItemClick(ordem.id) },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "OS #${ordem.id} - ${ordem.cliente}",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.MiddleEllipsis
                    )
                    Text(text = ordem.descricao)
                    if (ordem.urgente) {
                        Text(
                            text = "URGÊNCIA: ALTA",
                            color = Color(0xFFCE0058),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaVaziaPreview() {
    ViewModelNavigationTheme {
        ListaOrdensScreen(
            uiState = OrdemServicoUiState(),
            onNovaOrdemClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaPreenchidaPreview() {
    ViewModelNavigationTheme {
        ListaOrdensScreen(
            uiState = OrdemServicoUiState(
                ordens = listOf(
                    OrdemServico(
                        id = 1,
                        cliente = "João Guilherme",
                        descricao = "Trocar lâmpada da sala 64F",
                        urgente = false
                    ),
                    OrdemServico(
                        id = 2,
                        cliente = "José Carlos",
                        descricao = "Trocar projetor quebrado",
                        urgente = true
                    ),
                    OrdemServico(
                        id = 3,
                        cliente = "Um nome muito grande para testar quebra de linha bla bla bla",
                        descricao = "Uma descrição muito grande para testar quebra de linha bla bla bla",
                        urgente = true
                    )
                )
            ),
            onNovaOrdemClick = {},
            onItemClick = {}
        )
    }
}