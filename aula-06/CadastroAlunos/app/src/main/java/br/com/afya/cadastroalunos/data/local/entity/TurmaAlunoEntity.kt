package br.com.afya.cadastroalunos.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "turma_alunos",
    primaryKeys = ["turma_id", "aluno_id"],
    foreignKeys = [
        ForeignKey(
            entity = TurmaEntity::class,
            parentColumns = ["id"],
            childColumns = ["turma_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AlunoEntity::class,
            parentColumns = ["id"],
            childColumns = ["aluno_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["turma_id"]),
        Index(value = ["aluno_id"])
    ]
)
data class TurmaAlunoEntity(
    @ColumnInfo(name = "turma_id")
    val turmaId: Int,

    @ColumnInfo(name = "aluno_id")
    val alunoId: Int
)
