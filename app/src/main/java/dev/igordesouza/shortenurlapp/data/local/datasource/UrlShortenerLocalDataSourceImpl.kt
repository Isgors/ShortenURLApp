package dev.igordesouza.shortenurlapp.data.local.datasource

import dev.igordesouza.shortenurlapp.data.local.database.UrlDao
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity

class UrlShortenerLocalDataSourceImpl(private val urlDao: UrlDao) : UrlShortenerLocalDataSource {
    override suspend fun getRecentlyShortenedUrls(): List<UrlEntity> {
        return urlDao.getRecentlyShortenedUrls()
    }

    override suspend fun saveUrl(url: UrlEntity) {
        urlDao.insertUrl(url)
    }

    override suspend fun findByOriginalUrl(originalUrl: String): UrlEntity? {
        return urlDao.findByOriginalUrl(originalUrl)
    }

    override suspend fun deleteUrl(url: UrlEntity) {
        urlDao.deleteUrl(url)
    }

    override suspend fun deleteAllUrls() {
        urlDao.deleteAllUrls()
    }
}
