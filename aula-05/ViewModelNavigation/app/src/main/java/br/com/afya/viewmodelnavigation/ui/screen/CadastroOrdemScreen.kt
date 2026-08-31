package br.com.afya.viewmodelnavigation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.afya.viewmodelnavigation.ui.state.OrdemServicoUiState
import br.com.afya.viewmodelnavigation.ui.theme.ViewModelNavigationTheme

// TELA 2: FORMULÁRIO DE CADASTRO COM RECEBIMENTO DE CALLBACKS
@Composable
fun CadastroOrdemScreen(
    uiState: OrdemServicoUiState,
    onClienteChange: (String) -> Unit,
    onDescricaoChange: (String) -> Unit,
    onUrgenteChange: (Boolean) -> Unit,
    onSalvarClick: () -> Unit,
    onVoltarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar()
        }
    ) { padding ->
        ConteudoFormulario(
            uiState = uiState,
            onClienteChange = onClienteChange,
            onDescricaoChange = onDescricaoChange,
            onUrgenteChange = onUrgenteChange,
            onSalvarClick = onSalvarClick,
            onVoltarClick = onVoltarClick,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text("Nova Ordem de Serviço")
        }
    )
}

@Composable
private fun ConteudoFormulario(
    uiState: OrdemServicoUiState,
    onClienteChange: (String) -> Unit,
    onDescricaoChange: (String) -> Unit,
    onUrgenteChange: (Boolean) -> Unit,
    onSalvarClick: () -> Unit,
    onVoltarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.clienteInput,
            onValueChange = onClienteChange,
            label = {
                Text("Nome do Cliente")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.descricaoInput,
            onValueChange = onDescricaoChange,
            label = {
                Text("Descrição do Problema (mín. 10 caracteres)")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.urgente,
                onCheckedChange = onUrgenteChange
            )
            Text("Marcar como Urgência Técnica")
        }

        uiState.mensagemErro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSalvarClick,
            enabled = uiState.podeSalvar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCE0058))
        ) {
            Text("Salvar Ordem de Serviço")
        }
        TextButton(
            onClick = onVoltarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormularioVazioPreview() {
    ViewModelNavigationTheme {
        CadastroOrdemScreen(
            uiState = OrdemServicoUiState(),
            onClienteChange = {},
            onDescricaoChange = {},
            onUrgenteChange = {},
            onSalvarClick = {},
            onVoltarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormularioValidoPreview() {
    ViewModelNavigationTheme {
        CadastroOrdemScreen(
            uiState = OrdemServicoUiState(
                clienteInput = "Um nome muito grande para testar quebra de linha bla bla bla",
                descricaoInput = "Uma descrição muito grande para testar quebra de linha bla bla bla",
                urgente = true,
            ),
            onClienteChange = {},
            onDescricaoChange = {},
            onUrgenteChange = {},
            onSalvarClick = {},
            onVoltarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormularioInvalidoPreview() {
    ViewModelNavigationTheme {
        CadastroOrdemScreen(
            uiState = OrdemServicoUiState(
                clienteInput = "Um nome muito grande para testar quebra de linha bla bla bla",
                descricaoInput = "Descrição",
                urgente = true,
                mensagemErro = "A descrição deve ter, no mínimo, 10 caracteres"
            ),
            onClienteChange = {},
            onDescricaoChange = {},
            onUrgenteChange = {},
            onSalvarClick = {},
            onVoltarClick = {}
        )
    }
}