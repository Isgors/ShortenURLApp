package dev.igordesouza.shortenurlapp.data.repository

import dev.igordesouza.shortenurlapp.data.local.datasource.UrlShortenerLocalDataSource
import dev.igordesouza.shortenurlapp.data.mapper.toEntity
import dev.igordesouza.shortenurlapp.data.remote.datasource.UrlShortenerRemoteDataSource
import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UrlRepositoryImpl(
    private val remote: UrlShortenerRemoteDataSource,
    private val local: UrlShortenerLocalDataSource
) : UrlRepository {

    override fun observeUrls(): Flow<List<Url>> =
        local.observeUrls()

    override fun shortenUrl(url: String): Flow<Result<Unit>> = flow {
        val response = remote.shortenUrl(url)

        val domainUrl = Url(
            alias = response.alias,
            originalUrl = response.links.originalUrl,
            shortenedUrl = response.links.shortUrl
        )

        local.saveUrl(domainUrl.toEntity())

        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(e))
    }

    override suspend fun deleteUrl(url: Url) {
        local.delete(url.toEntity())
    }

    override suspend fun deleteAllUrls() {
        local.deleteAll()
    }
}
