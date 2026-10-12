package br.com.afya.cadastroalunos.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.cadastroalunos.data.local.AppDatabase
import br.com.afya.cadastroalunos.data.local.preferences.AppPreferencesManager
import br.com.afya.cadastroalunos.data.repository.AlunoRepository
import br.com.afya.cadastroalunos.data.repository.DisciplinaRepository
import br.com.afya.cadastroalunos.data.repository.TurmaRepository
import br.com.afya.cadastroalunos.model.OrdenacaoTurma
import br.com.afya.cadastroalunos.model.Turma
import br.com.afya.cadastroalunos.model.TurmaDetalhes
import br.com.afya.cadastroalunos.ui.state.DetalhesTurmaUiState
import br.com.afya.cadastroalunos.ui.state.ExclusaoTurmaUiState
import br.com.afya.cadastroalunos.ui.state.FormularioTurmaUiState
import br.com.afya.cadastroalunos.ui.state.ListaTurmasUiState
import br.com.afya.cadastroalunos.ui.state.SalvarTurmaUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TurmaViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: TurmaRepository = TurmaRepository(
        AppDatabase.obterInstancia(application).turmaDao()
    ),
    private val disciplinaRepository: DisciplinaRepository = DisciplinaRepository(
        AppDatabase.obterInstancia(application).disciplinaDao()
    ),
    private val alunoRepository: AlunoRepository = AlunoRepository(
        AppDatabase.obterInstancia(application).alunoDao()
    ),
    private val preferencesManager: AppPreferencesManager = AppPreferencesManager(application)
) : AndroidViewModel(application) {

    // ==========================================
    // Estados da tela de listagem
    // ==========================================
    var listaUiState: ListaTurmasUiState by mutableStateOf(ListaTurmasUiState.Carregando)
        private set

    var exclusaoUiState: ExclusaoTurmaUiState by mutableStateOf(ExclusaoTurmaUiState.Ocioso)
        private set

    var ordenacaoAtual: OrdenacaoTurma by mutableStateOf(preferencesManager.obterOrdenacaoTurma())
        private set

    private val ordenacaoFlow = MutableStateFlow(preferencesManager.obterOrdenacaoTurma())
    private var turmasJob: Job? = null

    // ==========================================
    // Estados da tela de formulário
    // ==========================================
    var formularioUiState: FormularioTurmaUiState by mutableStateOf(FormularioTurmaUiState.Ocioso)
        private set

    var salvarUiState: SalvarTurmaUiState by mutableStateOf(SalvarTurmaUiState.Ocioso)
        private set

    // ==========================================
    // Estados da tela de detalhes
    // ==========================================
    var detalhesUiState: DetalhesTurmaUiState by mutableStateOf(DetalhesTurmaUiState.Carregando)
        private set

    private var detalhesJob: Job? = null

    init {
        observarTurmas()
    }

    // ==========================================
    // Operações da tela de listagem
    // ==========================================

    fun carregarTurmas() {
        observarTurmas()
    }

    fun alterarOrdenacao(novaOrdenacao: OrdenacaoTurma) {
        ordenacaoAtual = novaOrdenacao
        preferencesManager.salvarOrdenacaoTurma(novaOrdenacao)
        ordenacaoFlow.value = novaOrdenacao
    }

    private fun observarTurmas() {
        turmasJob?.cancel()
        listaUiState = ListaTurmasUiState.Carregando
        turmasJob = viewModelScope.launch {
            repository.turmasEmTempoReal
                .combine(ordenacaoFlow) { turmas, ordenacao ->
                    ordenarTurmas(turmas, ordenacao)
                }
                .catch {
                    listaUiState = ListaTurmasUiState.Erro(
                        "Erro ao carregar turmas do banco local. Tente novamente."
                    )
                }
                .collect { turmasOrdenadas ->
                    listaUiState = ListaTurmasUiState.Sucesso(turmasOrdenadas)
                }
        }
    }

    private fun ordenarTurmas(turmas: List<TurmaDetalhes>, ordenacao: OrdenacaoTurma): List<TurmaDetalhes> {
        return when (ordenacao) {
            OrdenacaoTurma.NOME_ASC -> turmas.sortedBy { it.turma.nome.lowercase() }
            OrdenacaoTurma.NOME_DESC -> turmas.sortedByDescending { it.turma.nome.lowercase() }
            OrdenacaoTurma.DISCIPLINA_ASC -> turmas.sortedBy { it.disciplina?.nome?.lowercase() ?: "" }
            OrdenacaoTurma.DISCIPLINA_DESC -> turmas.sortedByDescending { it.disciplina?.nome?.lowercase() ?: "" }
            OrdenacaoTurma.ALUNOS_DESC -> turmas.sortedByDescending { it.alunos.size }
            OrdenacaoTurma.ALUNOS_ASC -> turmas.sortedBy { it.alunos.size }
        }
    }

    fun excluir(turma: Turma) {
        exclusaoUiState = ExclusaoTurmaUiState.Excluindo
        viewModelScope.launch {
            try {
                repository.removerTurma(turma)
                exclusaoUiState = ExclusaoTurmaUiState.Sucesso
            } catch (_: Exception) {
                exclusaoUiState = ExclusaoTurmaUiState.Erro(
                    "Erro ao excluir turma. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        exclusaoUiState = ExclusaoTurmaUiState.Ocioso
    }

    // ==========================================
    // Operações da tela de formulário
    // ==========================================

    fun prepararNovaTurma() {
        formularioUiState = FormularioTurmaUiState.Carregando
        salvarUiState = SalvarTurmaUiState.Ocioso
        viewModelScope.launch {
            try {
                val disciplinas = disciplinaRepository.disciplinasEmTempoReal.first()
                val alunos = alunoRepository.alunosEmTempoReal.first()
                formularioUiState = FormularioTurmaUiState.Sucesso(
                    turma = null,
                    disciplinas = disciplinas,
                    alunos = alunos
                )
            } catch (_: Exception) {
                formularioUiState = FormularioTurmaUiState.Erro(
                    "Erro ao preparar formulário de turma."
                )
            }
        }
    }

    fun carregarTurma(id: Int) {
        formularioUiState = FormularioTurmaUiState.Carregando
        salvarUiState = SalvarTurmaUiState.Ocioso
        viewModelScope.launch {
            try {
                val turma = repository.obterTurmaPorId(id)
                val disciplinas = disciplinaRepository.disciplinasEmTempoReal.first()
                val alunos = alunoRepository.alunosEmTempoReal.first()
                if (turma != null) {
                    formularioUiState = FormularioTurmaUiState.Sucesso(
                        turma = turma,
                        disciplinas = disciplinas,
                        alunos = alunos
                    )
                } else {
                    formularioUiState = FormularioTurmaUiState.Erro(
                        "Turma não encontrada no banco local."
                    )
                }
            } catch (_: Exception) {
                formularioUiState = FormularioTurmaUiState.Erro(
                    "Erro ao carregar turma. Tente novamente."
                )
            }
        }
    }

    fun salvar(turma: Turma) {
        salvarUiState = SalvarTurmaUiState.Salvando
        viewModelScope.launch {
            try {
                repository.salvarTurma(turma)
                salvarUiState = SalvarTurmaUiState.Sucesso
            } catch (_: Exception) {
                salvarUiState = SalvarTurmaUiState.Erro(
                    "Erro ao salvar turma no banco local. Tente novamente."
                )
            }
        }
    }

    fun limparSalvarUiState() {
        salvarUiState = SalvarTurmaUiState.Ocioso
    }

    // ==========================================
    // Operações da tela de detalhes
    // ==========================================

    fun carregarDetalhesTurma(id: Int) {
        detalhesJob?.cancel()
        detalhesUiState = DetalhesTurmaUiState.Carregando
        detalhesJob = viewModelScope.launch {
            repository.observarTurmaDetalhes(id)
                .combine(alunoRepository.alunosEmTempoReal) { turmaDetalhes, todosAlunos ->
                    if (turmaDetalhes == null) {
                        DetalhesTurmaUiState.Erro("Turma não encontrada no banco local.")
                    } else {
                        val idsMatriculados = turmaDetalhes.alunos.map { it.id }.toSet()
                        val disponiveis = todosAlunos.filter { it.id !in idsMatriculados }
                        DetalhesTurmaUiState.Sucesso(
                            turmaDetalhes = turmaDetalhes,
                            alunosDisponiveis = disponiveis
                        )
                    }
                }
                .catch {
                    detalhesUiState = DetalhesTurmaUiState.Erro(
                        "Erro ao carregar detalhes da turma. Tente novamente."
                    )
                }
                .collect { estado ->
                    detalhesUiState = estado
                }
        }
    }

    fun adicionarAlunoNaTurma(turmaId: Int, alunoId: Int) {
        viewModelScope.launch {
            try {
                repository.adicionarAlunoNaTurma(turmaId, alunoId)
            } catch (_: Exception) {
                // Tratamento em caso de erro
            }
        }
    }

    fun removerAlunoDaTurma(turmaId: Int, alunoId: Int) {
        viewModelScope.launch {
            try {
                repository.removerAlunoDaTurma(turmaId, alunoId)
            } catch (_: Exception) {
                // Tratamento em caso de erro
            }
        }
    }
}