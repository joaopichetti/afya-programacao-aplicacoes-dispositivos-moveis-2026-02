package br.com.afya.cadastroalunos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.afya.cadastroalunos.data.local.entity.TurmaAlunoEntity
import br.com.afya.cadastroalunos.data.local.entity.TurmaComDisciplinaEAlunos
import br.com.afya.cadastroalunos.data.local.entity.TurmaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TurmaDao {

    @Transaction
    @Query("SELECT * FROM turmas ORDER BY nome ASC")
    fun observarTodasTurmasCompletas(): Flow<List<TurmaComDisciplinaEAlunos>>

    @Transaction
    @Query("SELECT * FROM turmas WHERE id = :id")
    fun observarTurmaCompletaPorId(id: Int): Flow<TurmaComDisciplinaEAlunos?>

    @Query("SELECT * FROM turmas WHERE id = :id")
    suspend fun buscarPorId(id: Int): TurmaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(turma: TurmaEntity): Long

    @Update
    suspend fun atualizar(turma: TurmaEntity)

    @Delete
    suspend fun deletar(turma: TurmaEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserirTurmaAlunos(lista: List<TurmaAlunoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserirTurmaAluno(turmaAluno: TurmaAlunoEntity)

    @Query("DELETE FROM turma_alunos WHERE turma_id = :turmaId AND aluno_id = :alunoId")
    suspend fun removerAlunoDaTurma(turmaId: Int, alunoId: Int)

    @Query("DELETE FROM turma_alunos WHERE turma_id = :turmaId")
    suspend fun removerTodosAlunosDaTurma(turmaId: Int)

    @Query("SELECT aluno_id FROM turma_alunos WHERE turma_id = :turmaId")
    suspend fun buscarAlunoIdsPorTurma(turmaId: Int): List<Int>
}