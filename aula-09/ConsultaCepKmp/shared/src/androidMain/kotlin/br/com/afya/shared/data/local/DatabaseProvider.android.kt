package br.com.afya.shared.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private lateinit var applicationContext: Context

// Deve ser chamado pela aplicação Android antes de acessar o banco
fun initDatabase(context: Context) {
    applicationContext = context
}

internal actual fun databaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = applicationContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath
    )
}