package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.ShortenUrlOutcome
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShortenUrlUseCaseTest {

    private lateinit var urlRepository: UrlRepository
    private lateinit var useCase: ShortenUrlUseCaseImpl

    @Before
    fun setup() {
        urlRepository = mockk()
        useCase = ShortenUrlUseCaseImpl(urlRepository)
    }

    @Test
    fun `when input is blank then return EmptyInput`() = runTest {
        // Given
        val url = "   "

        // When
        val result = useCase(url).first()

        // Then
        assertEquals(ShortenUrlOutcome.EmptyInput, result)
    }

    @Test
    fun `when shortenUrl is successful then return Success`() = runTest {
        // Given
        val url = "https://www.google.com"
        every { urlRepository.shortenUrl(url) } returns flowOf(Result.success(Unit))

        // When
        val result = useCase(url).first()

        // Then
        assertEquals(ShortenUrlOutcome.Success, result)
    }

    @Test
    fun `when shortenUrl fails then return Error`() = runTest {
        // Given
        val url = "https://www.google.com"
        val errorMessage = "Network error"
        every { urlRepository.shortenUrl(url) } returns flowOf(Result.failure(Exception(errorMessage)))

        // When
        val result = useCase(url).first()

        // Then
        assertTrue(result is ShortenUrlOutcome.Error)
        assertEquals(errorMessage, (result as ShortenUrlOutcome.Error).message)
    }

    @Test
    fun `when shortenUrl fails with null message then return default Error message`() = runTest {
        // Given
        val url = "https://www.google.com"
        every { urlRepository.shortenUrl(url) } returns flowOf(Result.failure(Exception()))

        // When
        val result = useCase(url).first()

        // Then
        assertTrue(result is ShortenUrlOutcome.Error)
        assertEquals("Error shortening URL", (result as ShortenUrlOutcome.Error).message)
    }
}