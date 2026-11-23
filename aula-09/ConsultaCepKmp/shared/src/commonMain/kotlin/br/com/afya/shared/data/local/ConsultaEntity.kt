package br.com.afya.shared.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultas")
data class ConsultaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "cep_consultado")
    val cepConsultado: String,
    val encontrado: Boolean,
    val cep: String?,
    val logradouro: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?,
    @ColumnInfo(name = "data_consulta_utc")
    val dataConsultaUtc: Long
)