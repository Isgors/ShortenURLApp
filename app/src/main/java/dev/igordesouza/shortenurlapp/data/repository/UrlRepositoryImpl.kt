package dev.igordesouza.shortenurlapp.data.repository

import dev.igordesouza.shortenurlapp.data.local.datasource.UrlShortenerLocalDataSource
import dev.igordesouza.shortenurlapp.data.mapper.toDomain
import dev.igordesouza.shortenurlapp.data.mapper.toEntity
import dev.igordesouza.shortenurlapp.data.remote.datasource.UrlShortenerRemoteDataSource
import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UrlRepositoryImpl(
    private val remoteDataSource: UrlShortenerRemoteDataSource,
    private val localDataSource: UrlShortenerLocalDataSource
) : UrlRepository {

    override fun shortenUrl(url: String): Flow<Result<Url>> = flow {
        val response = remoteDataSource.shortenUrl(url)
        val domainUrl = Url(
            alias = response.alias,
            originalUrl = response.links.originalUrl,
            shortenedUrl = response.links.shortUrl
        )
        localDataSource.saveUrl(domainUrl.toEntity())
        emit(Result.success(domainUrl))
    }.catch { e ->
        emit(Result.failure(e as Exception))
    }

    override suspend fun getRecentlyShortenedUrls(): List<Url> {
        return try {
            localDataSource.getRecentlyShortenedUrls().toDomain()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun findByOriginalUrl(originalUrl: String): Url? {
        return localDataSource.findByOriginalUrl(originalUrl)?.toDomain()
    }

    override suspend fun deleteUrl(url: Url) {
        localDataSource.deleteUrl(url.toEntity())
    }

    override suspend fun deleteAllUrls() {
        localDataSource.deleteAllUrls()
    }
}
