package dev.igordesouza.shortenurlapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.igordesouza.shortenurlapp.presentation.MainActivity
import dev.igordesouza.shortenurlapp.robot.HomeRobot
import dev.igordesouza.shortenurlapp.robot.HomeRobotImpl
import dev.igordesouza.shortenurlapp.rule.RetryRule
import dev.igordesouza.shortenurlapp.rule.ScreenshotRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End-to-end tests for Home screen behavior.
 *
 * AAA pattern:
 * Arrange -> Act -> Assert
 *
 * Tests are fully isolated and order-independent.
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val screenshotRule = ScreenshotRule()

//    @get:Rule
//    val retryRule = RetryRule()

    private val robot: HomeRobot =
        HomeRobotImpl(composeRule)

    // ---------- ARRANGE HELPERS ----------

    private fun arrangeWithSingleUrl(
        url: String
    ): HomeRobot =
        robot.waitForHomeScreen()
            .enterUrl(url)
            .shortenUrl()
            .waitForAnyShortenedUrl()

    private fun arrangeWithMultipleUrls(urls: List<String>): HomeRobot {
        robot.waitForHomeScreen()

        urls.forEach {
            robot.enterUrl(it)
                .shortenUrl()
                .waitForAnyShortenedUrl()
        }

        return robot
    }

    // ---------- TESTS ----------

    @Test
    fun shortenUrl_happyPath() {
        robot.waitForHomeScreen()
            .enterUrl("https://www.google.com")
            .shortenUrl()
            .waitForShortenedUrlContaining("google")
    }

    @Test
    fun deleteAllUrls_removesAllItems() {
        arrangeWithMultipleUrls(listOf("https://www.google.com", "https://www.android.com"))

        robot.openDeleteAllDialog()
            .confirmDeletion()
            .assertEmptyStateVisible()
    }

    @Test
    fun shorten_copy_to_clipboard() {
        arrangeWithSingleUrl( "https://www.google.com")

        robot.openFirstUrlActions( "https://www.google.com")
            .copyUrl()
            .assertClipboardContains("http")
    }


    @Test
    fun openUrl_opensBrowser() {
        Intents.init()

        try {
            // Arrange
            arrangeWithSingleUrl("https://www.android.com")

            // Act
            robot.openFirstUrlActions("https://www.android.com")
                .clickOpenInBrowser()

            // Assert
            Intents.intended(
                allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasDataString(startsWith("http"))
                )
            )
        } finally {
            Intents.release()
        }
    }

    @Test
    fun openUrl_opensBrowser() {
        arrangeWithSingleUrl("https://www.android.com")

        robot.openFirstUrlActions("https://www.android.com")
            .openUrlInBrowser()
            .assertBrowserOpened()
    }
}

