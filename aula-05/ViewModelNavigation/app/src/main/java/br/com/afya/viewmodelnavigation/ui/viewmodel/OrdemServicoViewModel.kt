package br.com.afya.viewmodelnavigation.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.afya.viewmodelnavigation.data.OrdemServico
import br.com.afya.viewmodelnavigation.ui.state.OrdemServicoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrdemServicoViewModel : ViewModel() {
    // Estado mutável privado (Backing Property):
    // somente o ViewModel pode escrever
    private val _uiState = MutableStateFlow(OrdemServicoUiState())
    // Exposição pública imutável (Read-Only) para os Composables da View
    val uiState: StateFlow<OrdemServicoUiState> = _uiState.asStateFlow()
    private var ultimoId = 1

    // Processamento de eventos vindos da View (Events Up)
    fun onClienteChanged(novoCliente: String) {
        _uiState.update { estadoAtual ->
            estadoAtual.copy(
                clienteInput = novoCliente,
                mensagemErro = null
            )
        }
    }

    fun onDescricaoChanged(novaDescricao: String) {
        _uiState.update { estadoAtual ->
            estadoAtual.copy(
                descricaoInput = novaDescricao,
                mensagemErro = null
            )
        }
    }

    fun onUrgenteChanged(novoUrgente: Boolean) {
        _uiState.update { estadoAtual ->
            estadoAtual.copy(urgente = novoUrgente)
        }
    }

    fun salvarOrdem(onFinalizado: () -> Unit) {
        val estadoAtual = _uiState.value
        if (!estadoAtual.podeSalvar) {
            _uiState.update {
                it.copy(
                    mensagemErro = "Preencha o cliente e a descrição" +
                            " com no mínimo 10 caracteres."
                )
            }
            return
        }
        val novaOrdem = OrdemServico(
            id = ultimoId++,
            cliente = estadoAtual.clienteInput,
            descricao = estadoAtual.descricaoInput,
            urgente = estadoAtual.urgente
        )
        _uiState.update {
            it.copy(
                ordens = it.ordens + novaOrdem,
                clienteInput = "",
                descricaoInput = "",
                urgente = false,
                mensagemErro = null
            )
        }
        onFinalizado()
    }
}