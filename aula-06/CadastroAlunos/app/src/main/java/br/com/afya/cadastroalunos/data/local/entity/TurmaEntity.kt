package br.com.afya.cadastroalunos.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.afya.cadastroalunos.model.Turma

@Entity(
    tableName = "turmas",
    foreignKeys = [
        ForeignKey(
            entity = DisciplinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["disciplina_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["disciplina_id"])]
)
data class TurmaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "disciplina_id")
    val disciplinaId: Int
) {
    fun toTurma(alunoIds: List<Int> = emptyList()): Turma = Turma(
        id = id,
        nome = nome,
        disciplinaId = disciplinaId,
        alunoIds = alunoIds
    )

    companion object {
        fun fromTurma(turma: Turma): TurmaEntity = TurmaEntity(
            id = turma.id,
            nome = turma.nome,
            disciplinaId = turma.disciplinaId
        )
    }
}
