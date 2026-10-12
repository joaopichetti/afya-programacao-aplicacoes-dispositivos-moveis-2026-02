package br.com.afya.cadastroalunos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.afya.cadastroalunos.data.local.converter.Converters
import br.com.afya.cadastroalunos.data.local.dao.AlunoDao
import br.com.afya.cadastroalunos.data.local.dao.DisciplinaDao
import br.com.afya.cadastroalunos.data.local.entity.AlunoEntity
import br.com.afya.cadastroalunos.data.local.entity.DisciplinaEntity

@Database(
    entities = [AlunoEntity::class, DisciplinaEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alunoDao(): AlunoDao
    abstract fun disciplinaDao(): DisciplinaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `disciplinas` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `nome` TEXT NOT NULL,
                        `codigo` TEXT NOT NULL,
                        `carga_horaria` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cadastro_alunos_database.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instancia
                return instancia
            }
        }
    }
}