package br.com.afya.shared.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal const val DATABASE_NAME = "consulta_cep.db"

// Cada plataforma define onde o arquivo do banco será criado
internal expect fun databaseBuilder(): RoomDatabase.Builder<AppDatabase>

object DatabaseProvider {
    val database: AppDatabase by lazy {
        databaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}