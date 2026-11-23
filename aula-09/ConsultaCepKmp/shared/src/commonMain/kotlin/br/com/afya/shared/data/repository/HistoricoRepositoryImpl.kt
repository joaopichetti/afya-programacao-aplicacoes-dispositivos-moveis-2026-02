package br.com.afya.shared.data.repository

import br.com.afya.shared.data.local.ConsultaDao
import br.com.afya.shared.data.local.ConsultaEntity
import br.com.afya.shared.data.local.DatabaseProvider
import br.com.afya.shared.domain.model.Consulta
import br.com.afya.shared.domain.model.Endereco
import br.com.afya.shared.domain.repository.HistoricoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Instant

class HistoricoRepositoryImpl(
    private val dao: ConsultaDao
) : HistoricoRepository {

    constructor() : this(DatabaseProvider.database.consultaDao())

    override suspend fun salvarConsulta(
        cepConsultado: String,
        endereco: Endereco?
    ) {
        dao.inserir(
            ConsultaEntity(
                cepConsultado = cepConsultado,
                encontrado = endereco != null,
                cep = endereco?.cep,
                logradouro = endereco?.logradouro,
                bairro = endereco?.bairro,
                localidade = endereco?.cidade,
                uf = endereco?.uf,
                // Epoch em milissegundos é sempre relativo a UTC, independente do fuso do dispositivo
                dataConsultaUtc = Clock.System.now().toEpochMilliseconds()
            )
        )
    }

    override fun listarConsultas(): Flow<List<Consulta>> {
        return dao.listarTodos().map { consultas -> consultas.map { it.toDomain() } }
    }

    private fun ConsultaEntity.toDomain(): Consulta {
        return Consulta(
            id = id,
            cepConsultado = cepConsultado,
            endereco = if (encontrado) {
                Endereco(
                    cep = cep.orEmpty(),
                    logradouro = logradouro.orEmpty(),
                    bairro = bairro.orEmpty(),
                    cidade = localidade.orEmpty(),
                    uf = uf.orEmpty()
                )
            } else {
                null
            },
            dataConsulta = Instant.fromEpochMilliseconds(dataConsultaUtc)
        )
    }
}