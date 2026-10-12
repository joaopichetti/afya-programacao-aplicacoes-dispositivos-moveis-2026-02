package br.com.afya.cadastroalunos.model

import com.google.gson.annotations.SerializedName

data class Turma(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nome")
    val nome: String = "",
    @SerializedName("disciplinaId")
    val disciplinaId: Int = 0,
    @SerializedName("alunoIds")
    val alunoIds: List<Int> = emptyList()
)
