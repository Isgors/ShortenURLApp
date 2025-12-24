package dev.igordesouza.shortenurlapp.data.local.datasource

import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity

interface UrlShortenerLocalDataSource {
    suspend fun getRecentlyShortenedUrls(): List<UrlEntity>
    suspend fun saveUrl(url: UrlEntity)
    suspend fun findByOriginalUrl(originalUrl: String): UrlEntity?
    suspend fun deleteUrl(url: UrlEntity)
    suspend fun deleteAllUrls()
}
