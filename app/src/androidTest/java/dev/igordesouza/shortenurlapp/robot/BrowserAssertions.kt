package dev.igordesouza.shortenurlapp.robot

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
                    androidx.test.uiautomator.By.pkg(pkg).depth(0)
                ),
                5_000
            )
        }

        require(opened) {
            "No known browser app was launched"
        }
    }
}
