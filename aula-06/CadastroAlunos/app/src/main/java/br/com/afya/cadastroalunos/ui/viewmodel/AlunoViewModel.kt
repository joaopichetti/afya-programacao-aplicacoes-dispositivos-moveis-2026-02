package br.com.afya.cadastroalunos.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.afya.cadastroalunos.model.Aluno
import br.com.afya.cadastroalunos.ui.state.ExclusaoAlunoUiState
import br.com.afya.cadastroalunos.ui.state.FormularioAlunoUiState
import br.com.afya.cadastroalunos.ui.state.ListaAlunosUiState
import br.com.afya.cadastroalunos.ui.state.SalvarAlunoUiState

class AlunoViewModel : ViewModel() {

    private val _alunos = mutableListOf<Aluno>()
    private var proximoId = 1

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
        listaUiState = ListaAlunosUiState.Sucesso(_alunos.toList())
    }

    fun excluir(aluno: Aluno) {
        exclusaoUiState = ExclusaoAlunoUiState.Excluindo
        _alunos.removeAll { it.id == aluno.id }
        exclusaoUiState = ExclusaoAlunoUiState.Sucesso
        listaUiState = ListaAlunosUiState.Sucesso(_alunos.toList())
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
        val aluno = _alunos.find { it.id == id }
        if (aluno != null) {
            formularioUiState = FormularioAlunoUiState.Sucesso(aluno)
        } else {
            formularioUiState = FormularioAlunoUiState.Erro("Aluno não encontrado")
        }
    }

    fun salvar(aluno: Aluno) {
        salvarUiState = SalvarAlunoUiState.Salvando
        if (aluno.id == 0) {
            _alunos.add(aluno.copy(id = proximoId++))
        } else {
            val index = _alunos.indexOfFirst { it.id == aluno.id }
            if (index != -1) {
                _alunos[index] = aluno
            }
        }
        salvarUiState = SalvarAlunoUiState.Sucesso
    }

    fun limparSalvarUiState() {
        salvarUiState = SalvarAlunoUiState.Ocioso
    }
}