package br.com.afya.cadastroalunos.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.afya.cadastroalunos.model.Aluno

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormularioAlunoScreen(
    aluno: Aluno?,
    onSalvarClick: (Aluno) -> Unit,
    onVoltarClick: () -> Unit
) {
    val editando = aluno != null

    var nome by remember { mutableStateOf(aluno?.nome ?: "") }
    var idade by remember { mutableStateOf(aluno?.idade?.toString() ?: "") }
    var mensalidade by remember { mutableStateOf(aluno?.mensalidade?.toString() ?: "") }
    var ativo by remember { mutableStateOf(aluno?.ativo ?: true) }
    var linguagens by remember { mutableStateOf(aluno?.linguagens ?: emptyList()) }
    var novaLinguagem by remember { mutableStateOf("") }

    var nomeErro by remember { mutableStateOf(false) }
    var idadeErro by remember { mutableStateOf(false) }

    Scaffold(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = mensalidade,
                onValueChange = { mensalidade = it },
                label = { Text("Mensalidade") },
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
                    onCheckedChange = { ativo = it }
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
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (novaLinguagem.isNotBlank()) {
                            linguagens = linguagens + novaLinguagem.trim()
                            novaLinguagem = ""
                        }
                    }
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
                                linguagens = linguagens - linguagem
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editando) "Atualizar" else "Cadastrar")
            }
        }
    }
}