package br.com.afya.cadastroalunos.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.cadastroalunos.data.local.AppDatabase
import br.com.afya.cadastroalunos.data.repository.AlunoRepository
import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.ui.state.ExclusaoAlunoUiState
import br.com.afya.cadastroalunos.ui.state.FormularioAlunoUiState
import br.com.afya.cadastroalunos.ui.state.ListaAlunosUiState
import br.com.afya.cadastroalunos.ui.state.SalvarAlunoUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlunoViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: AlunoRepository = AlunoRepository(
        AppDatabase.obterInstancia(application).alunoDao()
    )
) : AndroidViewModel(application) {

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

    private var alunosJob: Job? = null

    init {
        observarAlunos()
    }

    override fun onCleared() {
        super.onCleared()
        alunosJob?.cancel()
    }

    // ============================
    // Operações da tela de listagem
    // ============================

    fun observarAlunos() {
        alunosJob?.cancel()
        listaUiState = ListaAlunosUiState.Carregando
        alunosJob = viewModelScope.launch {
            repository.alunosEmTempoReal
                .catch {
                    listaUiState = ListaAlunosUiState.Erro(
                        "Erro ao carregar alunos do banco local. Tente novamente"
                    )
                }
                .collect { alunos ->
                    listaUiState = ListaAlunosUiState.Sucesso(alunos)
                }
        }
    }

    fun excluir(aluno: Aluno) {
        exclusaoUiState = ExclusaoAlunoUiState.Excluindo
        viewModelScope.launch {
            try {
                repository.removerAluno(aluno)
                exclusaoUiState = ExclusaoAlunoUiState.Sucesso
            } catch (_: Exception) {
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
            formularioUiState = try {
                val aluno = repository.obterPorId(id)
                if (aluno != null) {
                    FormularioAlunoUiState.Sucesso(aluno)
                } else {
                    FormularioAlunoUiState.Erro(
                        "Aluno não encontrado no banco local."
                    )
                }
            } catch (_: Exception) {
                FormularioAlunoUiState.Erro(
                    "Erro ao carregar aluno. Tente novamente."
                )
            }
        }
    }

    fun salvar(aluno: Aluno) {
        salvarUiState = SalvarAlunoUiState.Salvando
        viewModelScope.launch {
            try {
                repository.salvarAluno(aluno)
                salvarUiState = SalvarAlunoUiState.Sucesso
            } catch (_: Exception) {
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