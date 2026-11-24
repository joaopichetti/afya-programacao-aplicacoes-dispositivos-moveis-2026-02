package br.com.afya.shared.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.shared.data.repository.HistoricoRepositoryImpl
import br.com.afya.shared.domain.model.Consulta
import br.com.afya.shared.domain.repository.HistoricoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoricoViewModel(
    repository: HistoricoRepository = HistoricoRepositoryImpl()
) : ViewModel() {
    val consultas: StateFlow<List<Consulta>> = repository.listarConsultas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}