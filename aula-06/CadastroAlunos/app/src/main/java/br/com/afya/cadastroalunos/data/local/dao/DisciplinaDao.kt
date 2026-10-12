package br.com.afya.cadastroalunos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.afya.cadastroalunos.data.local.entity.DisciplinaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DisciplinaDao {

    @Query("SELECT * FROM disciplinas ORDER BY nome ASC")
    fun observarTodasDisciplinas(): Flow<List<DisciplinaEntity>>

    @Query("SELECT * FROM disciplinas WHERE id = :id")
    suspend fun buscarPorId(id: Int): DisciplinaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOuAtualizar(disciplina: DisciplinaEntity): Long

    @Update
    suspend fun atualizar(disciplina: DisciplinaEntity)

    @Delete
    suspend fun deletar(disciplina: DisciplinaEntity)
}