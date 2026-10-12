package br.com.afya.cadastroalunos.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.model.Disciplina
import br.com.afya.cadastroalunos.model.Turma
import br.com.afya.cadastroalunos.ui.state.FormularioTurmaUiState
import br.com.afya.cadastroalunos.ui.state.SalvarTurmaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioTurmaScreen(
    formularioUiState: FormularioTurmaUiState,
    salvarUiState: SalvarTurmaUiState,
    onSalvarClick: (Turma) -> Unit,
    onVoltarClick: () -> Unit,
    onTentarNovamente: () -> Unit,
    onSalvoComSucesso: () -> Unit,
    onErroMostrado: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage ao estado de salvamento
    LaunchedEffect(salvarUiState) {
        when (salvarUiState) {
            is SalvarTurmaUiState.Sucesso -> {
                onSalvoComSucesso()
            }
            is SalvarTurmaUiState.Erro -> {
                snackbarHostState.showSnackbar(salvarUiState.mensagem)
                onErroMostrado()
            }
            else -> {}
        }
    }

    val editando = (formularioUiState as? FormularioTurmaUiState.Sucesso)?.turma != null

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editando) "Editar Turma" else "Nova Turma")
                },
                navigationIcon = {
                    IconButton(onClick = onVoltarClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (formularioUiState) {
                is FormularioTurmaUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FormularioTurmaUiState.Erro -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formularioUiState.mensagem,
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

                is FormularioTurmaUiState.Ocioso -> {
                    // Ocioso aguardando inicialização
                }

                is FormularioTurmaUiState.Sucesso -> {
                    FormularioTurmaConteudo(
                        turma = formularioUiState.turma,
                        disciplinas = formularioUiState.disciplinas,
                        alunos = formularioUiState.alunos,
                        salvando = salvarUiState is SalvarTurmaUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioTurmaConteudo(
    turma: Turma?,
    disciplinas: List<Disciplina>,
    alunos: List<Aluno>,
    salvando: Boolean,
    onSalvarClick: (Turma) -> Unit
) {
    val editando = turma != null

    var nome by remember(turma) { mutableStateOf(turma?.nome ?: "") }
    var disciplinaIdSelecionada by remember(turma) {
        mutableIntStateOf(turma?.disciplinaId ?: (disciplinas.firstOrNull()?.id ?: 0))
    }

    val alunoIdsSelecionados = remember(turma) {
        mutableStateListOf<Int>().apply {
            turma?.alunoIds?.let { addAll(it) }
        }
    }

    var nomeErro by remember { mutableStateOf(false) }
    var disciplinaErro by remember { mutableStateOf(false) }
    var dropdownExpandido by remember { mutableStateOf(false) }

    val disciplinaSelecionada = disciplinas.firstOrNull { it.id == disciplinaIdSelecionada }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nome da Turma
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                nomeErro = false
            },
            label = { Text("Nome ou Identificador da Turma") },
            placeholder = { Text("Ex: Turma A - 2026/2, Noturno - 4º Período") },
            isError = nomeErro,
            supportingText = {
                if (nomeErro) Text("Nome da turma é obrigatório")
            },
            enabled = !salvando,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Vínculo da Disciplina (Dropdown)
        Column {
            Text(
                text = "Disciplina",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (disciplinas.isEmpty()) {
                Text(
                    text = "Nenhuma disciplina cadastrada. Cadastre uma disciplina antes de criar a turma.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = disciplinaSelecionada?.let { "${it.nome} (${it.codigo})" } ?: "Selecione uma disciplina",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                onClick = { dropdownExpandido = !dropdownExpandido },
                                enabled = !salvando
                            ) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Selecionar disciplina")
                            }
                        },
                        isError = disciplinaErro,
                        supportingText = {
                            if (disciplinaErro) Text("Selecione uma disciplina válida")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !salvando) {
                                dropdownExpandido = true
                            },
                        enabled = !salvando
                    )

                    DropdownMenu(
                        expanded = dropdownExpandido,
                        onDismissRequest = { dropdownExpandido = false }
                    ) {
                        disciplinas.forEach { d ->
                            DropdownMenuItem(
                                text = { Text("${d.nome} (${d.codigo}) - ${d.cargaHoraria}h") },
                                onClick = {
                                    disciplinaIdSelecionada = d.id
                                    disciplinaErro = false
                                    dropdownExpandido = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Seleção de Alunos (Tabela associativa TurmaAluno)
        Column {
            Text(
                text = "Alunos da Turma (${alunoIdsSelecionados.size} selecionados)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (alunos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "Nenhum aluno cadastrado no sistema. Você poderá adicionar alunos posteriormente na tela de detalhes da turma.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        alunos.forEach { aluno ->
                            val estaSelecionado = aluno.id in alunoIdsSelecionados
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !salvando) {
                                        if (estaSelecionado) {
                                            alunoIdsSelecionados.remove(aluno.id)
                                        } else {
                                            alunoIdsSelecionados.add(aluno.id)
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = estaSelecionado,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            if (aluno.id !in alunoIdsSelecionados) {
                                                alunoIdsSelecionados.add(aluno.id)
                                            }
                                        } else {
                                            alunoIdsSelecionados.remove(aluno.id)
                                        }
                                    },
                                    enabled = !salvando
                                )
                                Column(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = aluno.nome,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Salvar
        Button(
            onClick = {
                nomeErro = nome.isBlank()
                disciplinaErro = disciplinaIdSelecionada <= 0

                if (!nomeErro && !disciplinaErro) {
                    val turmaSalvar = Turma(
                        id = turma?.id ?: 0,
                        nome = nome.trim(),
                        disciplinaId = disciplinaIdSelecionada,
                        alunoIds = alunoIdsSelecionados.toList()
                    )
                    onSalvarClick(turmaSalvar)
                }
            },
            enabled = !salvando && disciplinas.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (salvando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (editando) "Atualizar" else "Cadastrar")
            }
        }
    }
}