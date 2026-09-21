package br.com.afya.cadastroalunos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.afya.cadastroalunos.data.local.converter.Converters
import br.com.afya.cadastroalunos.data.local.dao.AlunoDao
import br.com.afya.cadastroalunos.data.local.entity.AlunoEntity

@Database(
    entities = [AlunoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alunoDao(): AlunoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cadastro_alunos_database.db"
                ).build()
                INSTANCE = instancia
                return instancia
            }
        }
    }
}