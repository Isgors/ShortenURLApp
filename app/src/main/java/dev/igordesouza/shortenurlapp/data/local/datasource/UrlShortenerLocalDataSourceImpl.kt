package dev.igordesouza.shortenurlapp.data.local.datasource

import dev.igordesouza.shortenurlapp.data.local.database.UrlDao
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity
import dev.igordesouza.shortenurlapp.data.mapper.toDomain
import dev.igordesouza.shortenurlapp.domain.model.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UrlShortenerLocalDataSourceImpl(
    private val dao: UrlDao
) : UrlShortenerLocalDataSource {

    override fun observeUrls(): Flow<List<Url>> =
        dao.observeUrls()
            .map { list -> list.map { it.toDomain() } }

    override suspend fun saveUrl(url: UrlEntity) {
        dao.insert(url)
    }

    override suspend fun delete(url: UrlEntity) {
        dao.delete(url)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
