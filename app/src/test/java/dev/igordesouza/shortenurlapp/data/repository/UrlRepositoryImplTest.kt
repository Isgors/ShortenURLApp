package dev.igordesouza.shortenurlapp.data.repository

import dev.igordesouza.shortenurlapp.data.local.datasource.UrlShortenerLocalDataSource
import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity
import dev.igordesouza.shortenurlapp.data.mapper.toDomain
import dev.igordesouza.shortenurlapp.data.remote.datasource.UrlShortenerRemoteDataSource
import dev.igordesouza.shortenurlapp.data.remote.model.AliasResponse
import dev.igordesouza.shortenurlapp.data.remote.model.Links
import dev.igordesouza.shortenurlapp.domain.model.Url
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UrlRepositoryImplTest {

    @Test
    fun `when shortenUrl is successful then url is saved and returned`() = runTest {
        // Given
        val localDataSource: UrlShortenerLocalDataSource = mockk(relaxed = true)
        val remoteDataSource: UrlShortenerRemoteDataSource = mockk()
        val repository = UrlRepositoryImpl(remoteDataSource, localDataSource)
        val url = "https://www.google.com"
        val aliasResponse = AliasResponse("alias", Links(url, "short"))
        coEvery { remoteDataSource.shortenUrl(url) } returns aliasResponse

        // When
        val result = repository.shortenUrl(url).first()

        // Then
        coVerify { localDataSource.saveUrl(any()) }
        assert(result.isSuccess)
    }

    @Test
    fun `when shortenUrl fails then url is not saved and error is returned`() = runTest {
        // Given
        val localDataSource: UrlShortenerLocalDataSource = mockk(relaxed = true)
        val remoteDataSource: UrlShortenerRemoteDataSource = mockk()
        val repository = UrlRepositoryImpl(remoteDataSource, localDataSource)
        val url = "https://www.google.com"
        val exception = Exception("Failed to shorten")
        coEvery { remoteDataSource.shortenUrl(url) } throws exception

        // When
        val result = repository.shortenUrl(url).first()

        // Then
        coVerify(exactly = 0) { localDataSource.saveUrl(any()) }
        assert(result.isFailure)
    }

    @Test
    fun `when getRecentlyShortenedUrls is called then local data source is called`() = runTest {
        // Given
        val localDataSource: UrlShortenerLocalDataSource = mockk()
        val remoteDataSource: UrlShortenerRemoteDataSource = mockk()
        val repository = UrlRepositoryImpl(remoteDataSource, localDataSource)
        val urlEntities = listOf(UrlEntity("alias", "original", "short"))
        coEvery { localDataSource.getRecentlyShortenedUrls() } returns urlEntities

        // When
        val result = repository.getRecentlyShortenedUrls()

        // Then
        assert(result == urlEntities.map { it.toDomain() })
    }

    @Test
    fun `when findByOriginalUrl is called then local data source is called`() = runTest {
        // Given
        val localDataSource: UrlShortenerLocalDataSource = mockk()
        val remoteDataSource: UrlShortenerRemoteDataSource = mockk()
        val repository = UrlRepositoryImpl(remoteDataSource, localDataSource)
        val originalUrl = "original"
        val urlEntity = UrlEntity("alias", originalUrl, "short")
        coEvery { localDataSource.findByOriginalUrl(originalUrl) } returns urlEntity

        // When
        val result = repository.findByOriginalUrl(originalUrl)

        // Then
        assert(result == urlEntity.toDomain())
    }

    @Test
    fun `when deleteUrl is called then local data source is called`() = runTest {
        // Given
        val localDataSource: UrlShortenerLocalDataSource = mockk(relaxed = true)
        val remoteDataSource: UrlShortenerRemoteDataSource = mockk()
        val repository = UrlRepositoryImpl(remoteDataSource, localDataSource)
        val url = Url("alias", "original", "short")

        // When
        repository.deleteUrl(url)

        // Then
        coVerify { localDataSource.deleteUrl(any()) }
    }

    @Test
    fun `when deleteAllUrls is called then local data source is called`() = runTest {
        // Given
        val localDataSource: UrlShortenerLocalDataSource = mockk(relaxed = true)
        val remoteDataSource: UrlShortenerRemoteDataSource = mockk()
        val repository = UrlRepositoryImpl(remoteDataSource, localDataSource)

        // When
        repository.deleteAllUrls()

        // Then
        coVerify { localDataSource.deleteAllUrls() }
    }
}
