package br.com.afya.cadastroalunos.model

import com.google.gson.annotations.SerializedName

data class Disciplina(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nome")
    val nome: String = "",
    @SerializedName("codigo")
    val codigo: String = "",
    @SerializedName("cargaHoraria")
    val cargaHoraria: Int = 0
)
