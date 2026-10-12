package br.com.afya.cadastroalunos.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.cadastroalunos.data.local.AppDatabase
import br.com.afya.cadastroalunos.data.local.preferences.AppPreferencesManager
import br.com.afya.cadastroalunos.data.repository.DisciplinaRepository
import br.com.afya.cadastroalunos.model.Disciplina
import br.com.afya.cadastroalunos.model.OrdenacaoDisciplina
import br.com.afya.cadastroalunos.ui.state.ExclusaoDisciplinaUiState
import br.com.afya.cadastroalunos.ui.state.FormularioDisciplinaUiState
import br.com.afya.cadastroalunos.ui.state.ListaDisciplinasUiState
import br.com.afya.cadastroalunos.ui.state.SalvarDisciplinaUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DisciplinaViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: DisciplinaRepository = DisciplinaRepository(
        AppDatabase.obterInstancia(application).disciplinaDao()
    ),
    private val preferencesManager: AppPreferencesManager = AppPreferencesManager(application)
) : AndroidViewModel(application) {

    // Estados da tela de listagem
    var listaUiState: ListaDisciplinasUiState by mutableStateOf(ListaDisciplinasUiState.Carregando)
        private set

    var exclusaoUiState: ExclusaoDisciplinaUiState by mutableStateOf(ExclusaoDisciplinaUiState.Ocioso)
        private set

    var ordenacaoAtual: OrdenacaoDisciplina by mutableStateOf(preferencesManager.obterOrdenacaoDisciplina())
        private set

    private val ordenacaoFlow = MutableStateFlow(preferencesManager.obterOrdenacaoDisciplina())

    // Estados da tela de formulário
    var formularioUiState: FormularioDisciplinaUiState by mutableStateOf(FormularioDisciplinaUiState.Ocioso)
        private set

    var salvarUiState: SalvarDisciplinaUiState by mutableStateOf(SalvarDisciplinaUiState.Ocioso)
        private set

    private var disciplinasJob: Job? = null

    init {
        observarDisciplinas()
    }

    // ============================
    // Operações da tela de listagem
    // ============================

    fun carregarDisciplinas() {
        observarDisciplinas()
    }

    fun alterarOrdenacao(novaOrdenacao: OrdenacaoDisciplina) {
        ordenacaoAtual = novaOrdenacao
        preferencesManager.salvarOrdenacaoDisciplina(novaOrdenacao)
        ordenacaoFlow.value = novaOrdenacao
    }

    private fun observarDisciplinas() {
        disciplinasJob?.cancel()
        listaUiState = ListaDisciplinasUiState.Carregando
        disciplinasJob = viewModelScope.launch {
            repository.disciplinasEmTempoReal
                .combine(ordenacaoFlow) { disciplinas, ordenacao ->
                    ordenarDisciplinas(disciplinas, ordenacao)
                }
                .catch {
                    listaUiState = ListaDisciplinasUiState.Erro(
                        "Erro ao carregar disciplinas do banco local. Tente novamente."
                    )
                }
                .collect { disciplinasOrdenadas ->
                    listaUiState = ListaDisciplinasUiState.Sucesso(disciplinasOrdenadas)
                }
        }
    }

    private fun ordenarDisciplinas(disciplinas: List<Disciplina>, ordenacao: OrdenacaoDisciplina): List<Disciplina> {
        return when (ordenacao) {
            OrdenacaoDisciplina.NOME_ASC -> disciplinas.sortedBy { it.nome.lowercase() }
            OrdenacaoDisciplina.NOME_DESC -> disciplinas.sortedByDescending { it.nome.lowercase() }
            OrdenacaoDisciplina.CODIGO_ASC -> disciplinas.sortedBy { it.codigo.lowercase() }
            OrdenacaoDisciplina.CODIGO_DESC -> disciplinas.sortedByDescending { it.codigo.lowercase() }
            OrdenacaoDisciplina.CARGA_HORARIA_ASC -> disciplinas.sortedBy { it.cargaHoraria }
            OrdenacaoDisciplina.CARGA_HORARIA_DESC -> disciplinas.sortedByDescending { it.cargaHoraria }
        }
    }

    fun excluir(disciplina: Disciplina) {
        exclusaoUiState = ExclusaoDisciplinaUiState.Excluindo
        viewModelScope.launch {
            try {
                repository.removerDisciplina(disciplina)
                exclusaoUiState = ExclusaoDisciplinaUiState.Sucesso
            } catch (e: Exception) {
                exclusaoUiState = ExclusaoDisciplinaUiState.Erro(
                    "Erro ao excluir disciplina. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        exclusaoUiState = ExclusaoDisciplinaUiState.Ocioso
    }

    // ================================
    // Operações da tela de formulário
    // ================================

    fun prepararNovaDisciplina() {
        formularioUiState = FormularioDisciplinaUiState.Ocioso
        salvarUiState = SalvarDisciplinaUiState.Ocioso
    }

    fun carregarDisciplina(id: Int) {
        formularioUiState = FormularioDisciplinaUiState.Carregando
        salvarUiState = SalvarDisciplinaUiState.Ocioso
        viewModelScope.launch {
            formularioUiState = try {
                val disciplina = repository.obterPorId(id)
                if (disciplina != null) {
                    FormularioDisciplinaUiState.Sucesso(disciplina)
                } else {
                    FormularioDisciplinaUiState.Erro(
                        "Disciplina não encontrada no banco local."
                    )
                }
            } catch (_: Exception) {
                FormularioDisciplinaUiState.Erro(
                    "Erro ao carregar disciplina. Tente novamente."
                )
            }
        }
    }

    fun salvar(disciplina: Disciplina) {
        salvarUiState = SalvarDisciplinaUiState.Salvando
        viewModelScope.launch {
            try {
                repository.salvarDisciplina(disciplina)
                salvarUiState = SalvarDisciplinaUiState.Sucesso
            } catch (_: Exception) {
                salvarUiState = SalvarDisciplinaUiState.Erro(
                    "Erro ao salvar disciplina no banco local. Tente novamente."
                )
            }
        }
    }

    fun limparSalvarUiState() {
        salvarUiState = SalvarDisciplinaUiState.Ocioso
    }
}