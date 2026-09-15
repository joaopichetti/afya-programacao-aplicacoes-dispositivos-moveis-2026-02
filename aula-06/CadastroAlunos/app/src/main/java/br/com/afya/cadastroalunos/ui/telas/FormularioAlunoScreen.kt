package br.com.afya.cadastroalunos.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.ui.state.FormularioAlunoUiState
import br.com.afya.cadastroalunos.ui.state.SalvarAlunoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAlunoScreen(
    formularioUiState: FormularioAlunoUiState,
    salvarUiState: SalvarAlunoUiState,
    onSalvarClick: (Aluno) -> Unit,
    onVoltarClick: () -> Unit,
    onTentarNovamente: () -> Unit,
    onSalvoComSucesso: () -> Unit,
    onErroMostrado: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage ao estado de salvamento
    LaunchedEffect(salvarUiState) {
        when (salvarUiState) {
            is SalvarAlunoUiState.Sucesso -> {
                onSalvoComSucesso()
            }
            is SalvarAlunoUiState.Erro -> {
                snackbarHostState.showSnackbar(salvarUiState.mensagem)
                onErroMostrado()
            }
            else -> {}
        }
    }

    val editando = formularioUiState is FormularioAlunoUiState.Sucesso

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editando) "Editar Aluno" else "Novo Aluno")
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
                is FormularioAlunoUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FormularioAlunoUiState.Erro -> {
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

                is FormularioAlunoUiState.Ocioso -> {
                    FormularioConteudo(
                        aluno = null,
                        salvando = salvarUiState is SalvarAlunoUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }

                is FormularioAlunoUiState.Sucesso -> {
                    FormularioConteudo(
                        aluno = formularioUiState.aluno,
                        salvando = salvarUiState is SalvarAlunoUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FormularioConteudo(
    aluno: Aluno?,
    salvando: Boolean,
    onSalvarClick: (Aluno) -> Unit
) {
    val editando = aluno != null

    var nome by remember(aluno) { mutableStateOf(aluno?.nome ?: "") }
    var idade by remember(aluno) { mutableStateOf(aluno?.idade?.toString() ?: "") }
    var mensalidade by remember(aluno) { mutableStateOf(aluno?.mensalidade?.toString() ?: "") }
    var ativo by remember(aluno) { mutableStateOf(aluno?.ativo ?: true) }
    var linguagens by remember(aluno) { mutableStateOf(aluno?.linguagens ?: emptyList()) }
    var novaLinguagem by remember { mutableStateOf("") }

    var nomeErro by remember { mutableStateOf(false) }
    var idadeErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                nomeErro = false
            },
            label = { Text("Nome") },
            isError = nomeErro,
            supportingText = {
                if (nomeErro) Text("Nome é obrigatório")
            },
            enabled = !salvando,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = idade,
            onValueChange = {
                idade = it
                idadeErro = false
            },
            label = { Text("Idade") },
            isError = idadeErro,
            supportingText = {
                if (idadeErro) Text("Idade deve ser um número válido")
            },
            enabled = !salvando,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = mensalidade,
            onValueChange = { mensalidade = it },
            label = { Text("Mensalidade") },
            enabled = !salvando,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ativo",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = ativo,
                onCheckedChange = { ativo = it },
                enabled = !salvando
            )
        }

        Text(
            text = "Linguagens",
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = novaLinguagem,
                onValueChange = { novaLinguagem = it },
                label = { Text("Adicionar linguagem") },
                enabled = !salvando,
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    if (novaLinguagem.isNotBlank()) {
                        linguagens = linguagens + novaLinguagem.trim()
                        novaLinguagem = ""
                    }
                },
                enabled = !salvando
            ) {
                Text("Adicionar")
            }
        }

        if (linguagens.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                linguagens.forEach { linguagem ->
                    InputChip(
                        selected = false,
                        onClick = {
                            if (!salvando) {
                                linguagens = linguagens - linguagem
                            }
                        },
                        label = { Text(linguagem) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remover $linguagem"
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nomeErro = nome.isBlank()
                idadeErro = idade.toIntOrNull() == null

                if (!nomeErro && !idadeErro) {
                    val alunoSalvar = Aluno(
                        id = aluno?.id ?: 0,
                        nome = nome.trim(),
                        idade = idade.toInt(),
                        mensalidade = mensalidade.toDoubleOrNull() ?: 0.0,
                        ativo = ativo,
                        linguagens = linguagens,
                        dataDeCadastro = aluno?.dataDeCadastro
                    )
                    onSalvarClick(alunoSalvar)
                }
            },
            enabled = !salvando,
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