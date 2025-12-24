package dev.igordesouza.shortenurlapp.domain.repository

import dev.igordesouza.shortenurlapp.domain.model.Url
import kotlinx.coroutines.flow.Flow

interface UrlRepository {
    fun shortenUrl(url: String): Flow<Result<Url>>
    suspend fun getRecentlyShortenedUrls(): List<Url>
    suspend fun findByOriginalUrl(originalUrl: String): Url?
    suspend fun deleteUrl(url: Url)
    suspend fun deleteAllUrls()
}
