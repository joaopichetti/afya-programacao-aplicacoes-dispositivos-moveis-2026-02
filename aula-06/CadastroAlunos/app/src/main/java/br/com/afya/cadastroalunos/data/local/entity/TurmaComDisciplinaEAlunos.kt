package br.com.afya.cadastroalunos.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.afya.cadastroalunos.model.TurmaDetalhes

data class TurmaComDisciplinaEAlunos(
    @Embedded
    val turma: TurmaEntity,

    @Relation(
        parentColumn = "disciplina_id",
        entityColumn = "id"
    )
    val disciplina: DisciplinaEntity?,

    @Relation(
        parentColumn = "id",
        entity = AlunoEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TurmaAlunoEntity::class,
            parentColumn = "turma_id",
            entityColumn = "aluno_id"
        )
    )
    val alunos: List<AlunoEntity> = emptyList()
) {
    fun toTurmaDetalhes(): TurmaDetalhes = TurmaDetalhes(
        turma = turma.toTurma(alunoIds = alunos.map { it.id }),
        disciplina = disciplina?.toDisciplina(),
        alunos = alunos.map { it.toAluno() }
    )
}
