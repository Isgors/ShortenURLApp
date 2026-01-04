package dev.igordesouza.shortenurlapp.util

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

object UiWaiter {

    fun waitForText(
        device: UiDevice,
        text: String,
        timeout: Long = 10_000
    ) {
        device.wait(
            Until.hasObject(By.textContains(text)),
            timeout
        )
    }

    fun waitForObject(
        device: UiDevice,
        selector: BySelector,
        timeout: Long = 10_000
    ) {
        device.wait(
            Until.hasObject(selector),
            timeout
        )
    }
}