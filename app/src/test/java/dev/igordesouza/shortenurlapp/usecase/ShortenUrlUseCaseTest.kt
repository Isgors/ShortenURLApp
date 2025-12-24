package dev.igordesouza.shortenurlapp.usecase

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ShortenUrlUseCaseTest {

    @Test
    fun `when url is blank then return failure`() = runTest {
        // Given
        val urlRepository: UrlRepository = mockk()
        val useCase = ShortenUrlUseCase(urlRepository)
        val url = ""

        // When
        val result = useCase(url).first()

        // Then
        assert(result.isFailure)
    }

    @Test
    fun `when url already exists then return failure`() = runTest {
        // Given
        val urlRepository: UrlRepository = mockk()
        val useCase = ShortenUrlUseCase(urlRepository)
        val url = "https://www.google.com"
        coEvery { urlRepository.findByOriginalUrl(url) } returns Url("alias", url, "short")

        // When
        val result = useCase(url).first()

        // Then
        assert(result.isFailure)
    }

    @Test
    fun `when url is new then return success`() = runTest {
        // Given
        val urlRepository: UrlRepository = mockk()
        val useCase = ShortenUrlUseCase(urlRepository)
        val url = "https://www.google.com"
        coEvery { urlRepository.findByOriginalUrl(url) } returns null
        coEvery { urlRepository.shortenUrl(url) } returns flowOf(Result.success(Url("alias", url, "short")))

        // When
        val result = useCase(url).first()

        // Then
        assert(result.isSuccess)
    }
}
