package br.com.afya.cadastroalunos.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import br.com.afya.cadastroalunos.model.Aluno

class AlunoViewModel : ViewModel() {

    private val _alunos = mutableStateListOf<Aluno>()
    val alunos: List<Aluno> get() = _alunos

    private var proximoId = 1

    fun inserir(aluno: Aluno) {
        val novoAluno = aluno.copy(id = proximoId++)
        _alunos.add(novoAluno)
    }

    fun atualizar(aluno: Aluno) {
        val index = _alunos.indexOfFirst { it.id == aluno.id }
        if (index != -1) {
            _alunos[index] = aluno
        }
    }

    fun excluir(aluno: Aluno) {
        _alunos.removeAll { it.id == aluno.id }
    }

    fun buscarPorId(id: Int): Aluno? {
        return _alunos.find { it.id == id }
    }
}