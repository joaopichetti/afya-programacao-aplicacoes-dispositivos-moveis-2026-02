package br.com.afya.cadastroalunos.data.repository

import br.com.afya.cadastroalunos.data.local.dao.TurmaDao
import br.com.afya.cadastroalunos.data.local.entity.TurmaAlunoEntity
import br.com.afya.cadastroalunos.data.local.entity.TurmaEntity
import br.com.afya.cadastroalunos.model.Turma
import br.com.afya.cadastroalunos.model.TurmaDetalhes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TurmaRepository(private val turmaDao: TurmaDao) {

    val turmasEmTempoReal: Flow<List<TurmaDetalhes>> =
        turmaDao.observarTodasTurmasCompletas().map { lista ->
            lista.map { it.toTurmaDetalhes() }
        }

    fun observarTurmaDetalhes(id: Int): Flow<TurmaDetalhes?> {
        return turmaDao.observarTurmaCompletaPorId(id).map { it?.toTurmaDetalhes() }
    }

    suspend fun obterTurmaPorId(id: Int): Turma? {
        val entity = turmaDao.buscarPorId(id) ?: return null
        val alunoIds = turmaDao.buscarAlunoIdsPorTurma(id)
        return entity.toTurma(alunoIds)
    }

    suspend fun salvarTurma(turma: Turma): Long {
        val entity = TurmaEntity.fromTurma(turma)
        val turmaId = if (entity.id == 0) {
            turmaDao.inserir(entity).toInt()
        } else {
            turmaDao.atualizar(entity)
            entity.id
        }

        // Sincroniza alunos na tabela associativa se especificados
        turmaDao.removerTodosAlunosDaTurma(turmaId)
        if (turma.alunoIds.isNotEmpty()) {
            val associacoes = turma.alunoIds.map { alunoId ->
                TurmaAlunoEntity(turmaId = turmaId, alunoId = alunoId)
            }
            turmaDao.inserirTurmaAlunos(associacoes)
        }

        return turmaId.toLong()
    }

    suspend fun removerTurma(turma: Turma) {
        turmaDao.deletar(TurmaEntity.fromTurma(turma))
    }

    suspend fun adicionarAlunoNaTurma(turmaId: Int, alunoId: Int) {
        turmaDao.inserirTurmaAluno(TurmaAlunoEntity(turmaId = turmaId, alunoId = alunoId))
    }

    suspend fun removerAlunoDaTurma(turmaId: Int, alunoId: Int) {
        turmaDao.removerAlunoDaTurma(turmaId = turmaId, alunoId = alunoId)
    }
}