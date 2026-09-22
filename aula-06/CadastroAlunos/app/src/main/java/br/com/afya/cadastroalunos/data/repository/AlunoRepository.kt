package br.com.afya.cadastroalunos.data.repository

import br.com.afya.cadastroalunos.data.local.dao.AlunoDao
import br.com.afya.cadastroalunos.data.local.entity.AlunoEntity
import br.com.afya.cadastroalunos.model.Aluno
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime

class AlunoRepository(private val alunoDao: AlunoDao) {

    val alunosEmTempoReal: Flow<List<Aluno>> = alunoDao
        .observarTodosAlunos()
        .map { lista ->
            lista.map { it.toAluno() }
        }

    suspend fun salvarAluno(aluno: Aluno): Long {
        val dataCadastro = aluno.dataDeCadastro
            ?: ZonedDateTime.now().toString()
        val alunoComData = aluno.copy(dataDeCadastro = dataCadastro)
        val entity = AlunoEntity.fromAluno(alunoComData)

        return if (entity.id == 0) {
            alunoDao.inserirOuAlterar(entity)
        } else {
            alunoDao.atualizar(entity)
            entity.id.toLong()
        }
    }

    suspend fun removerAluno(aluno: Aluno) {
        alunoDao.deletar(AlunoEntity.fromAluno(aluno))
    }

    suspend fun obterPorId(id: Int): Aluno? {
        return alunoDao.buscarPorId(id)?.toAluno()
    }
}