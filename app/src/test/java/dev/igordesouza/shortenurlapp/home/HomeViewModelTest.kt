package dev.igordesouza.shortenurlapp.home

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.GetRecentlyShortenedUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import dev.igordesouza.shortenurlapp.presentation.home.HomeUiState
import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModel
import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModelImpl
import dev.igordesouza.shortenurlapp.presentation.home.model.mapper.toDomain
import dev.igordesouza.shortenurlapp.presentation.home.model.mapper.toPresentation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val shortenUrlUseCase: ShortenUrlUseCase = mockk()
    private val getRecentlyShortenedUrlsUseCase: GetRecentlyShortenedUrlsUseCase = mockk()
    private val deleteUrlUseCase: DeleteUrlUseCase = mockk(relaxed = true)
    private val deleteAllUrlsUseCase: DeleteAllUrlsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModelImpl(
            shortenUrlUseCase,
            getRecentlyShortenedUrlsUseCase,
            deleteUrlUseCase,
            deleteAllUrlsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getRecentlyShortenedUrls success updates state to Idle with urls`() = runTest {
        // Given
        val urls = listOf(Url("alias", "original", "short"))
        coEvery { getRecentlyShortenedUrlsUseCase() } returns urls

        // When
        viewModel.getRecentlyShortenedUrls()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assert(state is HomeUiState.Idle)
        assert((state as HomeUiState.Idle).urls.isNotEmpty())
    }

    @Test
    fun `getRecentlyShortenedUrls failure updates state to Idle with error`() = runTest {
        // Given
        val exception = RuntimeException("Failed to fetch")
        coEvery { getRecentlyShortenedUrlsUseCase() } throws exception

        // When
        viewModel.getRecentlyShortenedUrls()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assert(state is HomeUiState.Idle)
        assert((state as HomeUiState.Idle).error == exception.message)
    }

    @Test
    fun `shortenUrl success clears input and updates list`() = runTest {
        // Given
        val initialUrl = "https://google.com"
        val newUrl = Url("newAlias", initialUrl, "shortNew")
        val updatedUrls = listOf(newUrl)

        // Set initial state to Idle
        coEvery { getRecentlyShortenedUrlsUseCase() } returns emptyList() andThen updatedUrls
        viewModel.getRecentlyShortenedUrls()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onUrlInputChanged(initialUrl)
        coEvery { shortenUrlUseCase(initialUrl) } returns flowOf(Result.success(newUrl))

        // When
        viewModel.shortenUrl()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value as HomeUiState.Idle
        assert(state.urls == updatedUrls.map { it.toPresentation() })
        assert(state.urlInput.isEmpty())
        assert(state.error == null)
    }

    @Test
    fun `shortenUrl failure shows error and keeps input`() = runTest {
        // Given
        val initialUrl = "https://google.com"
        val exception = RuntimeException("Shortening failed")

        // Set initial state to Idle
        coEvery { getRecentlyShortenedUrlsUseCase() } returns emptyList()
        viewModel.getRecentlyShortenedUrls()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onUrlInputChanged(initialUrl)
        coEvery { shortenUrlUseCase(initialUrl) } returns flowOf(Result.failure(exception))

        // When
        viewModel.shortenUrl()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value as HomeUiState.Idle
        assert(state.urlInput == initialUrl)
        assert(state.error != null)
    }

    @Test
    fun `shortenUrl does not run if input is blank`() = runTest {
        // Given
        viewModel.onUrlInputChanged("")

        // When
        viewModel.shortenUrl()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { shortenUrlUseCase(any()) }
    }

    @Test
    fun `deleteUrl refreshes url list`() = runTest {
        // Given
        val urlToDelete = Url("alias", "original", "short").toPresentation()
        coEvery { getRecentlyShortenedUrlsUseCase() } returns emptyList()

        // When
        viewModel.deleteUrl(urlToDelete)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { deleteUrlUseCase(urlToDelete.toDomain()) }
        coVerify { getRecentlyShortenedUrlsUseCase() }
        val state = viewModel.uiState.value as HomeUiState.Idle
        assert(state.urls.isEmpty())
    }

    @Test
    fun `deleteAllUrls refreshes url list`() = runTest {
        // Given
        coEvery { getRecentlyShortenedUrlsUseCase() } returns emptyList()

        // When
        viewModel.deleteAllUrls()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { deleteAllUrlsUseCase() }
        coVerify { getRecentlyShortenedUrlsUseCase() }
        val state = viewModel.uiState.value as HomeUiState.Idle
        assert(state.urls.isEmpty())
    }

    @Test
    fun `dismissError sets error to null`() = runTest {
        // Given
        val exception = RuntimeException("An error")
        coEvery { getRecentlyShortenedUrlsUseCase() } throws exception
        viewModel.getRecentlyShortenedUrls()
        testDispatcher.scheduler.advanceUntilIdle()
        assert((viewModel.uiState.value as HomeUiState.Idle).error != null)

        // When
        viewModel.dismissError()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert((viewModel.uiState.value as HomeUiState.Idle).error == null)
    }
}
