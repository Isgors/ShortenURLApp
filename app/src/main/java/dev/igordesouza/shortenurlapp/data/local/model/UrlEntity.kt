package dev.igordesouza.shortenurlapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "urls")
data class UrlEntity(
    @PrimaryKey
    val alias: String,
    val originalUrl: String,
    val shortenedUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)
