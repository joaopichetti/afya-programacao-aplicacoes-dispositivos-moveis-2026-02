package br.com.afya.cadastroalunos.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.network.RetrofitClient
import br.com.afya.cadastroalunos.ui.state.ExclusaoAlunoUiState
import br.com.afya.cadastroalunos.ui.state.FormularioAlunoUiState
import br.com.afya.cadastroalunos.ui.state.ListaAlunosUiState
import br.com.afya.cadastroalunos.ui.state.SalvarAlunoUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlunoViewModel : ViewModel() {

    private val apiService = RetrofitClient.alunoApiService

    // Estados da tela de listagem
    var listaUiState: ListaAlunosUiState by mutableStateOf(ListaAlunosUiState.Carregando)
        private set

    var exclusaoUiState: ExclusaoAlunoUiState by mutableStateOf(ExclusaoAlunoUiState.Ocioso)
        private set

    // Estados da tela de formulário
    var formularioUiState: FormularioAlunoUiState by mutableStateOf(FormularioAlunoUiState.Ocioso)
        private set

    var salvarUiState: SalvarAlunoUiState by mutableStateOf(SalvarAlunoUiState.Ocioso)
        private set

    init {
        carregarAlunos()
    }

    // ============================
    // Operações da tela de listagem
    // ============================

    fun carregarAlunos() {
        listaUiState = ListaAlunosUiState.Carregando
        viewModelScope.launch {
            delay(2000L)
            try {
                val alunos = apiService.listarAlunos()
                listaUiState = ListaAlunosUiState.Sucesso(alunos)
            } catch (e: Exception) {
                listaUiState = ListaAlunosUiState.Erro(
                    "Erro ao carregar alunos. Tente novamente."
                )
            }
        }
    }

    fun excluir(aluno: Aluno) {
        exclusaoUiState = ExclusaoAlunoUiState.Excluindo
        viewModelScope.launch {
            delay(2000L)
            try {
                apiService.excluirAluno(aluno.id)
                exclusaoUiState = ExclusaoAlunoUiState.Sucesso
                carregarAlunos()
            } catch (e: Exception) {
                exclusaoUiState = ExclusaoAlunoUiState.Erro(
                    "Erro ao excluir aluno. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        exclusaoUiState = ExclusaoAlunoUiState.Ocioso
    }

    // ================================
    // Operações da tela de formulário
    // ================================

    fun prepararNovoAluno() {
        formularioUiState = FormularioAlunoUiState.Ocioso
        salvarUiState = SalvarAlunoUiState.Ocioso
    }

    fun carregarAluno(id: Int) {
        formularioUiState = FormularioAlunoUiState.Carregando
        salvarUiState = SalvarAlunoUiState.Ocioso
        viewModelScope.launch {
            delay(2000L)
            try {
                val aluno = apiService.buscarAlunoPorId(id)
                formularioUiState = FormularioAlunoUiState.Sucesso(aluno)
            } catch (e: Exception) {
                formularioUiState = FormularioAlunoUiState.Erro(
                    "Erro ao carregar aluno. Tente novamente."
                )
            }
        }
    }

    fun salvar(aluno: Aluno) {
        salvarUiState = SalvarAlunoUiState.Salvando
        viewModelScope.launch {
            delay(2000L)
            try {
                if (aluno.id == 0) {
                    apiService.criarAluno(aluno)
                } else {
                    apiService.atualizarAluno(aluno.id, aluno)
                }
                salvarUiState = SalvarAlunoUiState.Sucesso
            } catch (e: Exception) {
                salvarUiState = SalvarAlunoUiState.Erro(
                    "Erro ao salvar aluno. Tente novamente."
                )
            }
        }
    }

    fun limparSalvarUiState() {
        salvarUiState = SalvarAlunoUiState.Ocioso
    }
}