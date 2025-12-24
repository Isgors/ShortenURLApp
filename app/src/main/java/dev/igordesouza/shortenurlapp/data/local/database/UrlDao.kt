package dev.igordesouza.shortenurlapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UrlDao {

    @Query("SELECT * FROM urls ORDER BY createdAt DESC")
    fun observeUrls(): Flow<List<UrlEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(url: UrlEntity)

    @Query("DELETE FROM urls")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(url: UrlEntity)
}
