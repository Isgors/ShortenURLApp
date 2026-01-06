package dev.igordesouza.shortenurlapp.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.igordesouza.shortenurlapp.category.ReleaseGate
import dev.igordesouza.shortenurlapp.category.SystemUi
import dev.igordesouza.shortenurlapp.presentation.MainActivity
import dev.igordesouza.shortenurlapp.home.robot.HomeRobotImpl
import dev.igordesouza.shortenurlapp.rule.FlakinessMetricRule
import dev.igordesouza.shortenurlapp.rule.RetryFlakyRule
import dev.igordesouza.shortenurlapp.rule.ScreenshotOnFailureRule
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith

/**
 * End-to-end tests for Home screen behavior.
 *
 * BDD pattern:
 * Given -> When -> Then
 *
 * Tests are fully isolated and order-independent.
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 1)
    val retryRule = RetryFlakyRule()

    @get:Rule(order = 2)
    val flakinessRule = FlakinessMetricRule("HomeScreenTest")

    @get:Rule(order = 3)
    val screenshotRule = ScreenshotOnFailureRule()

    private val robot by lazy {
        HomeRobotImpl(composeRule)
    }

    @Test
    @Category(ReleaseGate::class)
    fun givenValidUrl_whenUserShortens_thenShortenedUrlIsDisplayed() {
        robot
            .givenSingleShortenedUrl("https://www.google.com")
            .assertShortenedUrlDisplayed()
    }

    @Test
    @Category(ReleaseGate::class)
    fun givenMultipleShortenedUrls_whenDeleteAllConfirmed_thenEmptyStateIsShown() {
        robot
            .givenMultipleShortenedUrls(
                listOf("https://google.com", "https://android.com")
            )
            .assertEmptyStateVisible()
    }

    @Test
    @Category(SystemUi::class)
    fun givenShortenedUrl_whenUserCopies_thenClipboardContainsUrl() {
        robot.givenSingleShortenedUrl("https://google.com")
            .copyUrl()
            .assertClipboardContains("http")
    }

    @Test
    @Category(SystemUi::class)
    fun givenShortenedUrl_whenUserOpens_thenBrowserIsLaunched() {
        robot
            .givenSingleShortenedUrl("https://android.com")
            .assertBrowserOpened()
    }
}