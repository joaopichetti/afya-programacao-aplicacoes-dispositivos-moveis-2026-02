package br.com.afya.shared.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

internal actual fun databaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbDir = File(System.getProperty("user.home"), ".consultacepkmp")
        .apply { mkdirs() }
    val dbFile = File(dbDir, DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}