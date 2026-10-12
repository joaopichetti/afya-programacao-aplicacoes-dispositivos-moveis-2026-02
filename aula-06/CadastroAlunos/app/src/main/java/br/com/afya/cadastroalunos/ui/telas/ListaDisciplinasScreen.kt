package br.com.afya.cadastroalunos.ui.telas

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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.afya.cadastroalunos.model.Disciplina
import br.com.afya.cadastroalunos.model.OrdenacaoDisciplina
import br.com.afya.cadastroalunos.ui.state.ExclusaoDisciplinaUiState
import br.com.afya.cadastroalunos.ui.state.ListaDisciplinasUiState

@Composable
fun ListaDisciplinasScreen(
    listaUiState: ListaDisciplinasUiState,
    exclusaoUiState: ExclusaoDisciplinaUiState,
    ordenacaoAtual: OrdenacaoDisciplina,
    onOrdenacaoChange: (OrdenacaoDisciplina) -> Unit,
    onAdicionarClick: () -> Unit,
    onEditarClick: (Disciplina) -> Unit,
    onExcluirClick: (Disciplina) -> Unit,
    onTentarNovamente: () -> Unit,
    onExclusaoMensagemMostrada: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var disciplinaParaExcluir by remember { mutableStateOf<Disciplina?>(null) }

    // Reage aos estados da exclusão exibindo Snackbar
    LaunchedEffect(exclusaoUiState) {
        when (exclusaoUiState) {
            is ExclusaoDisciplinaUiState.Sucesso -> {
                snackbarHostState.showSnackbar("Disciplina excluída com sucesso")
                onExclusaoMensagemMostrada()
            }
            is ExclusaoDisciplinaUiState.Erro -> {
                snackbarHostState.showSnackbar(exclusaoUiState.mensagem)
                onExclusaoMensagemMostrada()
            }
            else -> {}
        }
    }

    // Diálogo de confirmação para exclusão de disciplina
    disciplinaParaExcluir?.let { disciplina ->
        ConfirmacaoExclusaoDisciplinaDialog(
            disciplina = disciplina,
            onConfirmar = {
                onExcluirClick(disciplina)
                disciplinaParaExcluir = null
            },
            onDispensar = {
                disciplinaParaExcluir = null
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListaDisciplinasTopAppBar(
                ordenacaoAtual = ordenacaoAtual,
                onOrdenacaoChange = onOrdenacaoChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionarClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar disciplina")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Indicador de progresso linear durante a exclusão
            if (exclusaoUiState is ExclusaoDisciplinaUiState.Excluindo) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (listaUiState) {
                is ListaDisciplinasUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListaDisciplinasUiState.Sucesso -> {
                    if (listaDisciplinasVazia(listaUiState)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhuma disciplina cadastrada",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(listaUiState.disciplinas, key = { it.id }) { disciplina ->
                                DisciplinaCard(
                                    disciplina = disciplina,
                                    onEditarClick = { onEditarClick(disciplina) },
                                    onExcluirClick = { disciplinaParaExcluir = disciplina }
                                )
                            }
                        }
                    }
                }

                is ListaDisciplinasUiState.Erro -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = listaUiState.mensagem,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = onTentarNovamente) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
        }
    }
}

private fun listaDisciplinasVazia(state: ListaDisciplinasUiState.Sucesso): Boolean {
    return state.disciplinas.isEmpty()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDisciplinasTopAppBar(
    ordenacaoAtual: OrdenacaoDisciplina,
    onOrdenacaoChange: (OrdenacaoDisciplina) -> Unit
) {
    TopAppBar(
        title = { Text("Cadastro de Disciplinas") },
        actions = {
            var menuAberto by remember { mutableStateOf(false) }

            IconButton(onClick = { menuAberto = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Ordenar disciplinas"
                )
            }

            DropdownMenu(
                expanded = menuAberto,
                onDismissRequest = { menuAberto = false }
            ) {
                OrdenacaoDisciplina.entries.forEach { opcao ->
                    val selecionado = opcao == ordenacaoAtual
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = opcao.titulo,
                                fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                color = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        trailingIcon = {
                            if (selecionado) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        onClick = {
                            menuAberto = false
                            onOrdenacaoChange(opcao)
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun ConfirmacaoExclusaoDisciplinaDialog(
    disciplina: Disciplina,
    onConfirmar: () -> Unit,
    onDispensar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDispensar,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = "Confirmar exclusão")
        },
        text = {
            Text(text = "Deseja realmente excluir a disciplina \"${disciplina.nome}\"?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmar
            ) {
                Text(
                    text = "Excluir",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDispensar
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DisciplinaCard(
    disciplina: Disciplina,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = disciplina.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onEditarClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onExcluirClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Excluir",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge/Chip compacto com o código ou sigla (ex.: PDM01, ED02)
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = disciplina.codigo.ifBlank { "S/C" },
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )

                // Carga horária
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${disciplina.cargaHoraria} horas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}