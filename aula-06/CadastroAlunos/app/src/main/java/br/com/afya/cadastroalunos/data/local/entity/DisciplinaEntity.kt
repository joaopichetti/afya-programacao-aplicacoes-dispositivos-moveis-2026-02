package br.com.afya.cadastroalunos.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.afya.cadastroalunos.model.Disciplina

@Entity(tableName = "disciplinas")
data class DisciplinaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "codigo")
    val codigo: String,

    @ColumnInfo(name = "carga_horaria")
    val cargaHoraria: Int
) {
    fun toDisciplina(): Disciplina = Disciplina(
        id = id,
        nome = nome,
        codigo = codigo,
        cargaHoraria = cargaHoraria
    )

    companion object {
        fun fromDisciplina(disciplina: Disciplina): DisciplinaEntity = DisciplinaEntity(
            id = disciplina.id,
            nome = disciplina.nome,
            codigo = disciplina.codigo,
            cargaHoraria = disciplina.cargaHoraria
        )
    }
}
