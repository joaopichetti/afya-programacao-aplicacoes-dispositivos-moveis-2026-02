package br.com.afya.cadastroalunos.data.local.preferences


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.afya.cadastroalunos.model.OrdenacaoAluno

class AppPreferencesManager(context: Context) {

    companion object {
        private const val PREFS_FILE = "cadastro_alunos_preferences"
        private const val KEY_ORDENACAO = "pref_ordenacao_alunos"
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
}