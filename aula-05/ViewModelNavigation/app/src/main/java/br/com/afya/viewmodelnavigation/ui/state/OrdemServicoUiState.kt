package br.com.afya.viewmodelnavigation.ui.state

import br.com.afya.viewmodelnavigation.data.OrdemServico

// UI State da Apresentação (View)
data class OrdemServicoUiState(
    val ordens: List<OrdemServico> = emptyList(),
    val clienteInput: String = "",
    val descricaoInput: String = "",
    val urgente: Boolean = false,
    val mensagemErro: String? = null
) {
    // Propriedade calculada para validação reativa de regras de tela
    val podeSalvar: Boolean
        get() = clienteInput.isNotBlank() &&
                descricaoInput.trim().length >= 10
}