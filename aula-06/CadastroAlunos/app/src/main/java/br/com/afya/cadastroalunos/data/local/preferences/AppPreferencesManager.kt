package br.com.afya.cadastroalunos.data.local.preferences


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.afya.cadastroalunos.model.OrdenacaoAluno
import br.com.afya.cadastroalunos.model.OrdenacaoDisciplina
import br.com.afya.cadastroalunos.model.OrdenacaoTurma

class AppPreferencesManager(context: Context) {

    companion object {
        private const val PREFS_FILE = "cadastro_alunos_preferences"
        private const val KEY_ORDENACAO = "pref_ordenacao_alunos"
        private const val KEY_ORDENACAO_DISCIPLINA = "pref_ordenacao_disciplinas"
        private const val KEY_ORDENACAO_TURMA = "pref_ordenacao_turmas"
    }

    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    fun salvarOrdenacao(ordenacao: OrdenacaoAluno) {
        sharedPreferences.edit { putString(KEY_ORDENACAO, ordenacao.name) }
    }

    fun obterOrdenacao(): OrdenacaoAluno {
        val nomeEnum = sharedPreferences.getString(KEY_ORDENACAO, OrdenacaoAluno.NOME_ASC.name)
        return try {
            OrdenacaoAluno.valueOf(nomeEnum ?: OrdenacaoAluno.NOME_ASC.name)
        } catch (_: Exception) {
            OrdenacaoAluno.NOME_ASC
        }
    }

    fun salvarOrdenacaoDisciplina(ordenacao: OrdenacaoDisciplina) {
        sharedPreferences.edit { putString(KEY_ORDENACAO_DISCIPLINA, ordenacao.name) }
    }

    fun obterOrdenacaoDisciplina(): OrdenacaoDisciplina {
        val nomeEnum = sharedPreferences.getString(KEY_ORDENACAO_DISCIPLINA, OrdenacaoDisciplina.NOME_ASC.name)
        return try {
            OrdenacaoDisciplina.valueOf(nomeEnum ?: OrdenacaoDisciplina.NOME_ASC.name)
        } catch (_: Exception) {
            OrdenacaoDisciplina.NOME_ASC
        }
    }

    fun salvarOrdenacaoTurma(ordenacao: OrdenacaoTurma) {
        sharedPreferences.edit { putString(KEY_ORDENACAO_TURMA, ordenacao.name) }
    }

    fun obterOrdenacaoTurma(): OrdenacaoTurma {
        val nomeEnum = sharedPreferences.getString(KEY_ORDENACAO_TURMA, OrdenacaoTurma.NOME_ASC.name)
        return try {
            OrdenacaoTurma.valueOf(nomeEnum ?: OrdenacaoTurma.NOME_ASC.name)
        } catch (_: Exception) {
            OrdenacaoTurma.NOME_ASC
        }
    }
}