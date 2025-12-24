package dev.igordesouza.shortenurlapp.domain.repository

import dev.igordesouza.shortenurlapp.domain.model.Url
import kotlinx.coroutines.flow.Flow

interface UrlRepository {
    fun observeUrls(): Flow<List<Url>>
    fun shortenUrl(url: String): Flow<Result<Unit>>
    suspend fun deleteUrl(url: Url)
    suspend fun deleteAllUrls()
}
