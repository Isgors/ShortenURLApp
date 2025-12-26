package dev.igordesouza.shortenurlapp.data.repository

import dev.igordesouza.shortenurlapp.data.local.datasource.UrlShortenerLocalDataSource
import dev.igordesouza.shortenurlapp.data.mapper.toEntity
import dev.igordesouza.shortenurlapp.data.remote.datasource.UrlShortenerRemoteDataSource
import dev.igordesouza.shortenurlapp.data.remote.model.AliasResponse
import dev.igordesouza.shortenurlapp.data.remote.model.Links
import dev.igordesouza.shortenurlapp.domain.model.Url
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UrlRepositoryImplTest {

    private lateinit var remote: UrlShortenerRemoteDataSource
    private lateinit var local: UrlShortenerLocalDataSource
    private lateinit var repository: UrlRepositoryImpl

    @Before
    fun setup() {
        remote = mockk()
        local = mockk()
        repository = UrlRepositoryImpl(remote, local)
    }

    @Test
    fun observeUrls_emitsUrlsFromLocal() = runTest {
        val urls = listOf(Url("alias", "original", "shortened"))
        every { local.observeUrls() } returns flowOf(urls)

        val result = repository.observeUrls().first()

        assertEquals(urls, result)
        coVerify { local.observeUrls() }
    }

    @Test
    fun shortenUrl_onSuccess_savesToLocalAndEmitsSuccess() = runTest {
        val urlStr = "https://google.com"
        val shortened = "https://short.en/alias"
        val alias = "alias"
        val response = AliasResponse(alias, Links(urlStr, shortened))

        coEvery { remote.shortenUrl(urlStr) } returns response
        coEvery { local.saveUrl(any()) } returns Unit

        val result = repository.shortenUrl(urlStr).first()

        assertTrue(result.isSuccess)
        coVerify { remote.shortenUrl(urlStr) }
        coVerify {
            local.saveUrl(match {
                it.alias == alias &&
                        it.originalUrl == urlStr &&
                        it.shortenedUrl == shortened
            })
        }
    }

    @Test
    fun shortenUrl_onFailure_emitsFailure() = runTest {
        val urlStr = "https://google.com"
        val exception = Exception("Remote error")
        coEvery { remote.shortenUrl(urlStr) } throws exception

        val result = repository.shortenUrl(urlStr).first()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 0) { local.saveUrl(any()) }
    }

    @Test
    fun deleteUrl_callsLocalDelete() = runTest {
        val alias = "alias"
        val original = "original"
        val shortened = "shortened"
        val url = Url(alias, original, shortened)
        coEvery { local.delete(any()) } returns Unit

        repository.deleteUrl(url)

        coVerify {
            local.delete(match {
                it.alias == alias &&
                        it.originalUrl == original &&
                        it.shortenedUrl == shortened
            })
        }
    }

    @Test
    fun deleteAllUrls_callsLocalDeleteAll() = runTest {
        coEvery { local.deleteAll() } returns Unit

        repository.deleteAllUrls()

        coVerify { local.deleteAll() }
    }
}