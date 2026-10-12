package br.com.afya.cadastroalunos.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.afya.cadastroalunos.model.Disciplina
import br.com.afya.cadastroalunos.ui.state.FormularioDisciplinaUiState
import br.com.afya.cadastroalunos.ui.state.SalvarDisciplinaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioDisciplinaScreen(
    formularioUiState: FormularioDisciplinaUiState,
    salvarUiState: SalvarDisciplinaUiState,
    onSalvarClick: (Disciplina) -> Unit,
    onVoltarClick: () -> Unit,
    onTentarNovamente: () -> Unit,
    onSalvoComSucesso: () -> Unit,
    onErroMostrado: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage ao estado de salvamento
    LaunchedEffect(salvarUiState) {
        when (salvarUiState) {
            is SalvarDisciplinaUiState.Sucesso -> {
                onSalvoComSucesso()
            }
            is SalvarDisciplinaUiState.Erro -> {
                snackbarHostState.showSnackbar(salvarUiState.mensagem)
                onErroMostrado()
            }
            else -> {}
        }
    }

    val editando = formularioUiState is FormularioDisciplinaUiState.Sucesso

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editando) "Editar Disciplina" else "Nova Disciplina")
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
                is FormularioDisciplinaUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FormularioDisciplinaUiState.Erro -> {
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

                is FormularioDisciplinaUiState.Ocioso -> {
                    FormularioDisciplinaConteudo(
                        disciplina = null,
                        salvando = salvarUiState is SalvarDisciplinaUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }

                is FormularioDisciplinaUiState.Sucesso -> {
                    FormularioDisciplinaConteudo(
                        disciplina = formularioUiState.disciplina,
                        salvando = salvarUiState is SalvarDisciplinaUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioDisciplinaConteudo(
    disciplina: Disciplina?,
    salvando: Boolean,
    onSalvarClick: (Disciplina) -> Unit
) {
    val editando = disciplina != null

    var nome by remember(disciplina) { mutableStateOf(disciplina?.nome ?: "") }
    var codigo by remember(disciplina) { mutableStateOf(disciplina?.codigo ?: "") }
    var cargaHoraria by remember(disciplina) { mutableStateOf(disciplina?.cargaHoraria?.takeIf { it > 0 }?.toString() ?: "") }

    var nomeErro by remember { mutableStateOf(false) }
    var codigoErro by remember { mutableStateOf(false) }
    var cargaHorariaErro by remember { mutableStateOf(false) }

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
            label = { Text("Nome da disciplina") },
            placeholder = { Text("Ex: Programação para Dispositivos Móveis") },
            isError = nomeErro,
            supportingText = {
                if (nomeErro) Text("Nome é obrigatório")
            },
            enabled = !salvando,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = codigo,
            onValueChange = {
                codigo = it.uppercase()
                codigoErro = false
            },
            label = { Text("Código ou Sigla") },
            placeholder = { Text("Ex: PDM01") },
            isError = codigoErro,
            supportingText = {
                if (codigoErro) Text("Código/Sigla é obrigatório")
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            enabled = !salvando,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = cargaHoraria,
            onValueChange = {
                cargaHoraria = it
                cargaHorariaErro = false
            },
            label = { Text("Carga horária (horas)") },
            placeholder = { Text("Ex: 60") },
            isError = cargaHorariaErro,
            supportingText = {
                if (cargaHorariaErro) Text("Informe uma carga horária válida (maior que 0)")
            },
            enabled = !salvando,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val cargaNum = cargaHoraria.toIntOrNull()
                nomeErro = nome.isBlank()
                codigoErro = codigo.isBlank()
                cargaHorariaErro = cargaNum == null || cargaNum <= 0

                if (!nomeErro && !codigoErro && !cargaHorariaErro) {
                    val disciplinaSalvar = Disciplina(
                        id = disciplina?.id ?: 0,
                        nome = nome.trim(),
                        codigo = codigo.trim(),
                        cargaHoraria = cargaNum ?: 0
                    )
                    onSalvarClick(disciplinaSalvar)
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