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
import br.com.afya.cadastroalunos.data.local.dao.TurmaDao
import br.com.afya.cadastroalunos.data.local.entity.AlunoEntity
import br.com.afya.cadastroalunos.data.local.entity.DisciplinaEntity
import br.com.afya.cadastroalunos.data.local.entity.TurmaAlunoEntity
import br.com.afya.cadastroalunos.data.local.entity.TurmaEntity

@Database(
    entities = [AlunoEntity::class, DisciplinaEntity::class, TurmaEntity::class, TurmaAlunoEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alunoDao(): AlunoDao
    abstract fun disciplinaDao(): DisciplinaDao
    abstract fun turmaDao(): TurmaDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `turmas` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `nome` TEXT NOT NULL,
                        `disciplina_id` INTEGER NOT NULL,
                        FOREIGN KEY(`disciplina_id`) REFERENCES `disciplinas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_turmas_disciplina_id` ON `turmas` (`disciplina_id`)")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `turma_alunos` (
                        `turma_id` INTEGER NOT NULL,
                        `aluno_id` INTEGER NOT NULL,
                        PRIMARY KEY(`turma_id`, `aluno_id`),
                        FOREIGN KEY(`turma_id`) REFERENCES `turmas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(`aluno_id`) REFERENCES `aluno`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_turma_alunos_turma_id` ON `turma_alunos` (`turma_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_turma_alunos_aluno_id` ON `turma_alunos` (`aluno_id`)")
            }
        }

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cadastro_alunos_database.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instancia
                return instancia
            }
        }
    }
}