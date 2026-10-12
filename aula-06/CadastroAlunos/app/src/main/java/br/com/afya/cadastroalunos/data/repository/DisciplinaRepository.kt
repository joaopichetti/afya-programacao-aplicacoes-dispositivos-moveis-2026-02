package br.com.afya.cadastroalunos.data.repository

import br.com.afya.cadastroalunos.data.local.dao.DisciplinaDao
import br.com.afya.cadastroalunos.data.local.entity.DisciplinaEntity
import br.com.afya.cadastroalunos.model.Disciplina
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DisciplinaRepository(private val disciplinaDao: DisciplinaDao) {

    val disciplinasEmTempoReal: Flow<List<Disciplina>> = disciplinaDao.observarTodasDisciplinas().map { lista ->
        lista.map { it.toDisciplina() }
    }

    suspend fun salvarDisciplina(disciplina: Disciplina): Long {
        val entity = DisciplinaEntity.fromDisciplina(disciplina)
        return if (entity.id == 0) {
            disciplinaDao.inserirOuAtualizar(entity)
        } else {
            disciplinaDao.atualizar(entity)
            entity.id.toLong()
        }
    }

    suspend fun removerDisciplina(disciplina: Disciplina) {
        disciplinaDao.deletar(DisciplinaEntity.fromDisciplina(disciplina))
    }

    suspend fun obterPorId(id: Int): Disciplina? {
        return disciplinaDao.buscarPorId(id)?.toDisciplina()
    }
}