package dev.igordesouza.shortenurlapp.home

import app.cash.turbine.test
import dev.igordesouza.shortenurlapp.domain.fakes.FakeData
import dev.igordesouza.shortenurlapp.domain.fakes.FakeDeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.fakes.FakeDeleteUrlUseCase
import dev.igordesouza.shortenurlapp.domain.fakes.FakeObserveUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.fakes.FakeShortenUrlUseCase
import dev.igordesouza.shortenurlapp.domain.model.ShortenUrlOutcome
import dev.igordesouza.shortenurlapp.presentation.home.HomeEffect
import dev.igordesouza.shortenurlapp.presentation.home.HomeIntent
import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModel
import dev.igordesouza.shortenurlapp.presentation.home.model.toPresentation
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    private lateinit var fakeObserveUrls: FakeObserveUrlsUseCase
    private lateinit var fakeShortenUrl: FakeShortenUrlUseCase
    private val fakeDeleteUrl = FakeDeleteUrlUseCase()
    private val fakeDeleteAll = FakeDeleteAllUrlsUseCase()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        fakeObserveUrls = FakeObserveUrlsUseCase()
        fakeShortenUrl = FakeShortenUrlUseCase()

        viewModel = HomeViewModel(
            observeUrlsUseCase = fakeObserveUrls,
            shortenUrlUseCase = fakeShortenUrl,
            deleteUrlUseCase = fakeDeleteUrl,
            deleteAllUrlsUseCase = fakeDeleteAll
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty input emits error effect`() = runTest {
        viewModel.dispatch(HomeIntent.ShortenClicked)

        viewModel.effect.test {
            assertEquals(
                HomeEffect.ShowError("URL cannot be empty"),
                awaitItem()
            )
        }
    }

    @Test
    fun `success clears input and scrolls`() = runTest {
        fakeShortenUrl.emit(ShortenUrlOutcome.Success)

        viewModel.dispatch(HomeIntent.UrlInputChanged("https://google.com"))
        viewModel.dispatch(HomeIntent.ShortenClicked)

        advanceUntilIdle()

        assertEquals("", viewModel.state.value.urlInput)

        viewModel.effect.test {
            assertEquals(
                HomeEffect.ScrollToIndex(0),
                awaitItem()
            )
        }
    }

    @Test
    fun `error outcome emits error effect`() = runTest {
        fakeShortenUrl.emit(
            ShortenUrlOutcome.Error("Network error")
        )

        viewModel.dispatch(HomeIntent.ShortenClicked)

        viewModel.effect.test {
            assertEquals(
                HomeEffect.ShowError("Network error"),
                awaitItem()
            )
        }
    }

    @Test
    fun `observed urls update state`() = runTest {
        val urls = FakeData.urls(2)

        fakeObserveUrls.emit(urls)
        advanceUntilIdle()

        assertEquals(urls.toPresentation(), viewModel.state.value.urls)
    }


}