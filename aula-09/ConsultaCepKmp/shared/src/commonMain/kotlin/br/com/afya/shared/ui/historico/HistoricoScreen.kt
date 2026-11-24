package br.com.afya.shared.ui.historico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.afya.shared.domain.model.Consulta
import br.com.afya.shared.domain.model.Endereco
import br.com.afya.shared.ui.formatarCep
import br.com.afya.shared.ui.formatarDataHora
import br.com.afya.shared.ui.theme.ConsultaCepKmpTheme
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoricoScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoricoViewModel = viewModel { HistoricoViewModel() },
    onVoltarClick: () -> Unit
) {
    val consultas by viewModel.consultas.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Consultas") },
                navigationIcon = {
                    IconButton(onClick = onVoltarClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) {  innerPadding ->
        if (consultas.isEmpty()) {
            HistoricoVazioContent(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = consultas, key = { it.id }) { consulta ->
                    ConsultaItem(consulta = consulta)
                }
            }
        }
    }
}

@Composable
private fun HistoricoVazioContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Nenhuma consulta realizada até o momento",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ConsultaItem(
    modifier: Modifier = Modifier,
    consulta: Consulta
) {
    val endereco = consulta.endereco

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (endereco != null) {
                    Icons.Default.LocationOn
                } else {
                    Icons.Default.Warning
                },
                contentDescription = null,
                tint = if (endereco != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.padding(start = 12.dp))
            Column {
                Text(
                    text = formatarCep(consulta.cepConsultado),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                if (endereco != null) {
                    Text(
                        text = listOf(endereco.logradouro, endereco.bairro)
                            .filter{ it.isNotBlank() }
                            .joinToString(" - ")
                            .ifBlank { "Logradouro não informado" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${endereco.cidade} - ${endereco.uf}".trim('-', ' '),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "CEP não encontrado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formatarDataHora(consulta.dataConsulta),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsultaItemPreview() {
    ConsultaCepKmpTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ConsultaItem(
                consulta = Consulta(
                    id = 1,
                    cepConsultado = "01001000",
                    endereco = Endereco(
                        cep = "01001-000",
                        logradouro = "Praça da Sé",
                        bairro = "Sé",
                        cidade = "São Paulo",
                        uf = "SP"
                    ),
                    dataConsulta = Instant.parse("2026-11-16T15:30:00Z")
                )
            )
            ConsultaItem(
                consulta = Consulta(
                    id = 2,
                    cepConsultado = "99999999",
                    endereco = null,
                    dataConsulta = Instant.parse("2026-11-16T15:32:10Z")
                )
            )
        }
    }
}