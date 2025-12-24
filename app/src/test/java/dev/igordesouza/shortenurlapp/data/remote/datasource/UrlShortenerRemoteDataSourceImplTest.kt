package dev.igordesouza.shortenurlapp.data.remote.datasource

import dev.igordesouza.shortenurlapp.data.remote.model.AliasRequest
import dev.igordesouza.shortenurlapp.data.remote.network.UrlShortenerService
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UrlShortenerRemoteDataSourceImplTest {

    @Test
    fun `when shortenUrl is called then service is called with correct request`() = runTest {
        // Given
        val service: UrlShortenerService = mockk(relaxed = true)
        val dataSource = UrlShortenerRemoteDataSourceImpl(service)
        val url = "https://www.google.com"

        // When
        dataSource.shortenUrl(url)

        // Then
        coVerify { service.postAlias(AliasRequest(url)) }
    }
}
