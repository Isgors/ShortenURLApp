package dev.igordesouza.shortenurlapp.home.robot

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

object BrowserAssertions {

    private val knownBrowsers = listOf(
        "com.android.chrome",
        "org.mozilla.firefox",
        "com.microsoft.emmx",
        "com.brave.browser"
    )

    fun assertBrowserOpened(device: UiDevice) {
        val opened = knownBrowsers.any { pkg ->
            device.wait(
                Until.hasObject(
                    By.pkg(pkg).depth(0)
                ),
                5_000
            )
        }

        require(opened) {
            "No known browser app was launched"
        }
    }
}