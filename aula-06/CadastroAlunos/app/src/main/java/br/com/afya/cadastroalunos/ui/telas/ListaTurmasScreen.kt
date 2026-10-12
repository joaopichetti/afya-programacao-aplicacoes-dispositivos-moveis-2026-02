package br.com.afya.cadastroalunos.ui.telas

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.afya.cadastroalunos.model.OrdenacaoTurma
import br.com.afya.cadastroalunos.model.Turma
import br.com.afya.cadastroalunos.model.TurmaDetalhes
import br.com.afya.cadastroalunos.ui.state.ExclusaoTurmaUiState
import br.com.afya.cadastroalunos.ui.state.ListaTurmasUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTurmasScreen(
    listaUiState: ListaTurmasUiState,
    exclusaoUiState: ExclusaoTurmaUiState,
    ordenacaoAtual: OrdenacaoTurma,
    onOrdenacaoChange: (OrdenacaoTurma) -> Unit,
    onAdicionarClick: () -> Unit,
    onDetalhesClick: (Turma) -> Unit,
    onEditarClick: (Turma) -> Unit,
    onExcluirClick: (Turma) -> Unit,
    onTentarNovamente: () -> Unit,
    onExclusaoMensagemMostrada: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var turmaParaExcluir by remember { mutableStateOf<Turma?>(null) }

    // Reage aos estados da exclusão exibindo Snackbar
    LaunchedEffect(exclusaoUiState) {
        when (exclusaoUiState) {
            is ExclusaoTurmaUiState.Sucesso -> {
                snackbarHostState.showSnackbar("Turma excluída com sucesso")
                onExclusaoMensagemMostrada()
            }
            is ExclusaoTurmaUiState.Erro -> {
                snackbarHostState.showSnackbar(exclusaoUiState.mensagem)
                onExclusaoMensagemMostrada()
            }
            else -> {}
        }
    }

    // Diálogo de confirmação para exclusão de turma
    turmaParaExcluir?.let { turma ->
        ConfirmacaoExclusaoTurmaDialog(
            turma = turma,
            onConfirmar = {
                onExcluirClick(turma)
                turmaParaExcluir = null
            },
            onDispensar = {
                turmaParaExcluir = null
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListaTurmasTopAppBar(
                ordenacaoAtual = ordenacaoAtual,
                onOrdenacaoChange = onOrdenacaoChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionarClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar turma")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Indicador de progresso durante a exclusão
            if (exclusaoUiState is ExclusaoTurmaUiState.Excluindo) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (listaUiState) {
                is ListaTurmasUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListaTurmasUiState.Sucesso -> {
                    if (listaUiState.turmas.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhuma turma cadastrada",
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
                            items(listaUiState.turmas, key = { it.turma.id }) { item ->
                                TurmaCard(
                                    turmaDetalhes = item,
                                    onCardClick = { onDetalhesClick(item.turma) },
                                    onEditarClick = { onEditarClick(item.turma) },
                                    onExcluirClick = { turmaParaExcluir = item.turma }
                                )
                            }
                        }
                    }
                }

                is ListaTurmasUiState.Erro -> {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTurmasTopAppBar(
    ordenacaoAtual: OrdenacaoTurma,
    onOrdenacaoChange: (OrdenacaoTurma) -> Unit
) {
    TopAppBar(
        title = { Text("Cadastro de Turmas") },
        actions = {
            var menuAberto by remember { mutableStateOf(false) }

            IconButton(onClick = { menuAberto = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Ordenar turmas"
                )
            }

            DropdownMenu(
                expanded = menuAberto,
                onDismissRequest = { menuAberto = false }
            ) {
                OrdenacaoTurma.entries.forEach { opcao ->
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
fun ConfirmacaoExclusaoTurmaDialog(
    turma: Turma,
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
            Text(text = "Deseja realmente excluir a turma \"${turma.nome}\"? Os vínculos com os alunos também serão excluídos.")
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(
                    text = "Excluir",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDispensar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun TurmaCard(
    turmaDetalhes: TurmaDetalhes,
    onCardClick: () -> Unit,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Linha superior: Nome da Turma e Ações
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = turmaDetalhes.turma.nome,
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

            Spacer(modifier = Modifier.height(4.dp))

            // Linha intermediária: Disciplina com Ícone de Livro
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = turmaDetalhes.disciplina?.nome ?: "Sem disciplina vinculada",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Linha inferior: Chip do Código da Disciplina e Contagem de Alunos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chip compacto com o código da disciplina (ex.: PDM01)
                SuggestionChip(
                    onClick = onCardClick,
                    label = {
                        Text(
                            text = turmaDetalhes.disciplina?.codigo?.ifBlank { "S/C" } ?: "S/C",
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )

                // Quantidade de alunos com largura protegida
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${turmaDetalhes.alunos.size} ${if (turmaDetalhes.alunos.size == 1) "aluno" else "alunos"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        softWrap = false
                    )
                }
            }
        }
    }
}