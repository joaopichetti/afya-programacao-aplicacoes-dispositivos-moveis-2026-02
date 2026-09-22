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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.Text
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
import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.model.OrdenacaoAluno
import br.com.afya.cadastroalunos.ui.state.ExclusaoAlunoUiState
import br.com.afya.cadastroalunos.ui.state.ListaAlunosUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaAlunosScreen(
    listaUiState: ListaAlunosUiState,
    exclusaoUiState: ExclusaoAlunoUiState,
    ordenacaoAtual: OrdenacaoAluno,
    onOrdenacaoChange: (OrdenacaoAluno) -> Unit,
    onAdicionarClick: () -> Unit,
    onEditarClick: (Aluno) -> Unit,
    onExcluirClick: (Aluno) -> Unit,
    onTentarNovamente: () -> Unit,
    onExclusaoMensagemMostrada: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage aos estados da exclusão exibindo Snackbar
    LaunchedEffect(exclusaoUiState) {
        when (exclusaoUiState) {
            is ExclusaoAlunoUiState.Sucesso -> {
                snackbarHostState.showSnackbar("Aluno excluído com sucesso")
                onExclusaoMensagemMostrada()
            }
            is ExclusaoAlunoUiState.Erro -> {
                snackbarHostState.showSnackbar(exclusaoUiState.mensagem)
                onExclusaoMensagemMostrada()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Alunos") },
                actions = {
                    var menuAberto by remember { mutableStateOf(false) }

                    IconButton(onClick = { menuAberto = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Ordenar alunos"
                        )
                    }

                    DropdownMenu(
                        expanded = menuAberto,
                        onDismissRequest = { menuAberto = false }
                    ) {
                        OrdenacaoAluno.entries.forEach { opcao ->
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionarClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar aluno")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Indicador de progresso linear durante a exclusão
            if (exclusaoUiState is ExclusaoAlunoUiState.Excluindo) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (listaUiState) {
                is ListaAlunosUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListaAlunosUiState.Sucesso -> {
                    if (listaUiState.alunos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum aluno cadastrado",
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
                            items(listaUiState.alunos, key = { it.id }) { aluno ->
                                AlunoCard(
                                    aluno = aluno,
                                    onEditarClick = { onEditarClick(aluno) },
                                    onExcluirClick = { onExcluirClick(aluno) }
                                )
                            }
                        }
                    }
                }

                is ListaAlunosUiState.Erro -> {
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

@Composable
fun AlunoCard(
    aluno: Aluno,
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
                    text = aluno.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
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

            Text(
                text = "Idade: ${aluno.idade} anos",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Mensalidade: R$ ${"%.2f".format(aluno.mensalidade)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${if (aluno.ativo) "Ativo" else "Inativo"}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (aluno.ativo)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            if (aluno.linguagens.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Linguagens: ${aluno.linguagens.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}