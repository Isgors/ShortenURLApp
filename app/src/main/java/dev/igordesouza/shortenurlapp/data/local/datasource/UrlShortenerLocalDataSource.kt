package dev.igordesouza.shortenurlapp.data.local.datasource

import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity
import dev.igordesouza.shortenurlapp.domain.model.Url
import kotlinx.coroutines.flow.Flow

interface UrlShortenerLocalDataSource {
    fun observeUrls(): Flow<List<Url>>

    suspend fun saveUrl(url: UrlEntity)

    suspend fun delete(url: UrlEntity)

    suspend fun deleteAll()
}
