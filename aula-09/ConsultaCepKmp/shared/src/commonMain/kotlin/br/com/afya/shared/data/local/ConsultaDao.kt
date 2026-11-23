package br.com.afya.shared.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultaDao {

    @Insert
    suspend fun inserir(consulta: ConsultaEntity)

    @Query("SELECT * FROM consultas ORDER BY data_consulta_utc DESC")
    fun listarTodos(): Flow<List<ConsultaEntity>>
}