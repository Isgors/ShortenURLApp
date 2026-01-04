package dev.igordesouza.shortenurlapp.rule

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File

class ScreenshotRule : TestWatcher() {

    override fun failed(e: Throwable?, description: Description) {
        val device = UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )

        val dir = File(
            InstrumentationRegistry.getInstrumentation()
                .targetContext
                .externalCacheDir,
            "screenshots"
        )

        if (!dir.exists()) dir.mkdirs()

        val file = File(
            dir,
            "${description.className}_${description.methodName}.png"
        )


        device.takeScreenshot(
            file,
            1.0f,
            100
        )

    }
}
