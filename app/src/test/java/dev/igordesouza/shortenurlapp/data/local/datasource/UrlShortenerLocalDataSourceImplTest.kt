package dev.igordesouza.shortenurlapp.data.local.datasource

import dev.igordesouza.shortenurlapp.data.local.database.UrlDao
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UrlShortenerLocalDataSourceImplTest {

    @Test
    fun `when saveUrl is called then dao is called`() = runTest {
        // Given
        val dao: UrlDao = mockk(relaxed = true)
        val dataSource = UrlShortenerLocalDataSourceImpl(dao)
        val urlEntity = UrlEntity("alias", "original", "short")

        // When
        dataSource.saveUrl(urlEntity)

        // Then
        coVerify { dao.insertUrl(urlEntity) }
    }

    @Test
    fun `when getRecentlyShortenedUrls is called then dao is called`() = runTest {
        // Given
        val dao: UrlDao = mockk(relaxed = true)
        val dataSource = UrlShortenerLocalDataSourceImpl(dao)

        // When
        dataSource.getRecentlyShortenedUrls()

        // Then
        coVerify { dao.getRecentlyShortenedUrls() }
    }

    @Test
    fun `when findByOriginalUrl is called then dao is called`() = runTest {
        // Given
        val dao: UrlDao = mockk(relaxed = true)
        val dataSource = UrlShortenerLocalDataSourceImpl(dao)
        val originalUrl = "original"

        // When
        dataSource.findByOriginalUrl(originalUrl)

        // Then
        coVerify { dao.findByOriginalUrl(originalUrl) }
    }

    @Test
    fun `when deleteUrl is called then dao is called`() = runTest {
        // Given
        val dao: UrlDao = mockk(relaxed = true)
        val dataSource = UrlShortenerLocalDataSourceImpl(dao)
        val urlEntity = UrlEntity("alias", "original", "short")

        // When
        dataSource.deleteUrl(urlEntity)

        // Then
        coVerify { dao.deleteUrl(urlEntity) }
    }

    @Test
    fun `when deleteAllUrls is called then dao is called`() = runTest {
        // Given
        val dao: UrlDao = mockk(relaxed = true)
        val dataSource = UrlShortenerLocalDataSourceImpl(dao)

        // When
        dataSource.deleteAllUrls()

        // Then
        coVerify { dao.deleteAllUrls() }
    }
}
