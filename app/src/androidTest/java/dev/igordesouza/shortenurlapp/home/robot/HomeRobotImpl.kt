package dev.igordesouza.shortenurlapp.home.robot

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import dev.igordesouza.shortenurlapp.home.robot.BrowserAssertions

/**
 * Concrete implementation of [HomeRobot].
 * Robot responsible ONLY for Compose UI interactions.
 * This robot is:
 * - Fast
 * - Deterministic
 * - Safe for release gates
 */
class HomeRobotImpl(
    private val composeRule: ComposeTestRule,
    private val device: UiDevice = UiDevice.getInstance(
        InstrumentationRegistry.getInstrumentation()
    ),
    private val context: Context = ApplicationProvider.getApplicationContext()
) : HomeRobot {

    override fun givenSingleShortenedUrl(url: String): HomeRobot {
        return this
            .enterUrl(url)
            .shortenUrl()
    }

    override fun givenMultipleShortenedUrls(urls: List<String>): HomeRobot {
        urls.forEach {
            this.enterUrl(it).shortenUrl()
        }
        return this
    }
    override fun enterUrl(url: String): HomeRobot = apply {
        composeRule.onNodeWithTag("UrlInput")
            .performTextInput(url)
    }

    override fun shortenUrl(): HomeRobot = apply {
        composeRule.onNodeWithTag("ShortenButton")
            .performClick()
    }

    override fun assertShortenedUrlDisplayed(): HomeRobot = apply {
        composeRule.onNodeWithTag("UrlItem")
            .assertExists()
    }

    override fun assertEmptyStateVisible(): HomeRobot = apply {
        composeRule.onNodeWithTag("EmptyState")
            .assertExists()
    }

    override fun copyUrl(): HomeRobot {
        device.findObject(By.textContains("Copy"))
            ?.click()
        return this
    }

    override fun assertClipboardContains(text: String): HomeRobot {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        require(
            clipboard.primaryClip
                ?.getItemAt(0)
                ?.text
                ?.contains(text) == true
        )
        return this
    }

    override fun assertBrowserOpened(): HomeRobot {
        BrowserAssertions.assertBrowserOpened(device)
        return this
    }

}
