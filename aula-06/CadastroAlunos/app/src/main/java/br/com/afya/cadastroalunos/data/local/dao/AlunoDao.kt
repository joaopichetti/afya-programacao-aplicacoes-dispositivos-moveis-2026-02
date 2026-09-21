package br.com.afya.cadastroalunos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.afya.cadastroalunos.data.local.entity.AlunoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlunoDao {
    @Query("SELECT * FROM aluno ORDER BY nome ASC")
    fun observarTodosAlunos(): Flow<List<AlunoEntity>>
    @Query("SELECT * FROM aluno WHERE id = :id")
    suspend fun buscarPorId(id: Int): AlunoEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOuAlterar(aluno: AlunoEntity): Long
    @Update
    suspend fun atualizar(aluno: AlunoEntity)
    @Delete
    suspend fun deletar(aluno: AlunoEntity)
}