package dev.igordesouza.shortenurlapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity

@Database(entities = [UrlEntity::class], version = 1)
abstract class UrlShortenerDatabase : RoomDatabase() {
    abstract fun urlDao(): UrlDao
}
