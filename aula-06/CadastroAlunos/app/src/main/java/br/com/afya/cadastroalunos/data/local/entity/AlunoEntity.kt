package br.com.afya.cadastroalunos.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.afya.cadastroalunos.model.Aluno

@Entity(tableName = "aluno")
data class AlunoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "nome")
    val nome: String,
    @ColumnInfo(name = "idade")
    val idade: Int,
    @ColumnInfo(name = "mensalidade")
    val mensalidade: Double,
    @ColumnInfo(name = "ativo")
    val ativo: Boolean,
    @ColumnInfo(name = "linguagens")
    val linguagens: List<String> = emptyList(),
    @ColumnInfo(name = "data_cadastro")
    val dataDeCadastro: String? = null
) {
    fun toAluno(): Aluno = Aluno(
        id = id,
        nome = nome,
        idade = idade,
        mensalidade = mensalidade,
        ativo = ativo,
        linguagens = linguagens,
        dataDeCadastro = dataDeCadastro
    )

    companion object {
        fun fromAluno(aluno: Aluno): AlunoEntity = AlunoEntity(
            id = aluno.id,
            nome = aluno.nome,
            idade = aluno.idade,
            mensalidade = aluno.mensalidade,
            ativo = aluno.ativo,
            linguagens = aluno.linguagens,
            dataDeCadastro = aluno.dataDeCadastro
        )
    }
}
