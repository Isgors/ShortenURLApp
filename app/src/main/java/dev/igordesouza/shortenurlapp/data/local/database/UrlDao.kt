package dev.igordesouza.shortenurlapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity

@Dao
interface UrlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrl(url: UrlEntity)

    @Query("SELECT * FROM urls ORDER BY createdAt DESC")
    suspend fun getRecentlyShortenedUrls(): List<UrlEntity>

    @Query("SELECT * FROM urls WHERE originalUrl = :originalUrl LIMIT 1")
    suspend fun findByOriginalUrl(originalUrl: String): UrlEntity?

    @Delete
    suspend fun deleteUrl(url: UrlEntity)

    @Query("DELETE FROM urls")
    suspend fun deleteAllUrls()
}
