package dev.igordesouza.shortenurlapp.robot

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

/**
 * Concrete implementation of [HomeRobot].
 *
 * Uses:
 * - ComposeTestRule for app UI (fast, deterministic)
 * - UiAutomator for system UI (bottom sheets, browser, clipboard)
 */
class HomeRobotImpl(
    private val composeRule: ComposeTestRule
) : HomeRobot {

    private val device: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val context: Context =
        ApplicationProvider.getApplicationContext()

    // ------------------ ARRANGE ------------------

    override fun waitForHomeScreen(): HomeRobot = apply {
        device.wait(
            Until.hasObject(By.text("URL Shortener")),
            TIMEOUT
        )
    }

    // ------------------ ACTIONS (Compose) ------------------

    override fun enterUrl(url: String): HomeRobot = apply {
        composeRule
            .onNodeWithTag("UrlInput")
            .performTextInput(url)
    }

    override fun shortenUrl(): HomeRobot = apply {
        composeRule
            .onNodeWithTag("ShortenButton")
            .performClick()
    }

    override fun waitForAnyShortenedUrl(): HomeRobot = apply {
        device.wait(
            Until.hasObject(By.res("UrlItem")),
            TIMEOUT
        )
    }

    override fun waitForShortenedUrlContaining(text: String): HomeRobot = apply {
        device.wait(
            Until.hasObject(By.textContains(text)),
            TIMEOUT
        )
    }

    // ------------------ ACTIONS (Hybrid) ------------------

    /**
     * Opens the bottom sheet / actions menu for the FIRST URL item.
     *
     * Uses Compose to deterministically select the first item
     * instead of relying on text or index guessing.
     */
    override fun openFirstUrlActions(text: String): HomeRobot = apply {
        composeRule
            .onAllNodes(hasTestTag("UrlItem_$text"))
            .onFirst()
            .performClick()
    }

    override fun openDeleteAllDialog(): HomeRobot = apply {
        device.findObject(By.text("Delete All"))?.click()
    }

    override fun confirmDeletion(): HomeRobot = apply {
        device.wait(
            Until.findObject(By.text("Confirm")),
            TIMEOUT
        )?.click()
    }

    override fun copyUrl(): HomeRobot = apply {
        device.wait(
            Until.findObject(By.textContains("Copy")),
            TIMEOUT
        )?.click()
    }

    override fun openUrlInBrowser(): HomeRobot = apply {
        device.wait(
            Until.findObject(By.textContains("Open")),
            TIMEOUT
        )?.click()
    }

    // ------------------ ASSERTIONS ------------------

    override fun assertClipboardContains(text: String): HomeRobot = apply {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipboardText = clipboard.primaryClip
            ?.getItemAt(0)
            ?.coerceToText(context)
            ?.toString()

        require(clipboardText?.contains(text) == true) {
            "Expected clipboard to contain <$text> but was <$clipboardText>"
        }
    }

    override fun assertBrowserOpened(): HomeRobot = apply {
        BrowserAssertions.assertBrowserOpened(device)
    }

    override fun assertEmptyStateVisible(): HomeRobot = apply {
        device.wait(
            Until.hasObject(By.text("No shortened URLs yet")),
            TIMEOUT
        )
    }

    companion object {
        private const val TIMEOUT = 5_000L
    }
}
