package br.com.afya.shared.domain.repository

import br.com.afya.shared.domain.model.Consulta
import br.com.afya.shared.domain.model.Endereco
import kotlinx.coroutines.flow.Flow

interface HistoricoRepository {
    suspend fun salvarConsulta(cepConsultado: String, endereco: Endereco?)

    fun listarConsultas(): Flow<List<Consulta>>
}