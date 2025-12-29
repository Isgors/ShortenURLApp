package dev.igordesouza.orthos.plugin.integration

import dev.igordesouza.orthos.plugin.util.OrthosGradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OrthosPluginBuildTypeTest {

    @Test
    fun `orthos instrumentation runs on release build`() {
        val projectDir = File("src/test/fixtures/app-release")

        val result = OrthosGradleRunner.run(projectDir, "assembleRelease")

        assertEquals(
            TaskOutcome.SUCCESS,
            result.task(":assembleRelease")?.outcome
        )

        assertTrue(result.output.contains("ORTHOS"))
    }

    @Test
    fun `orthos instrumentation does not run on debug build`() {
        val projectDir = File("src/test/fixtures/app-debug")

        val result = OrthosGradleRunner.run(projectDir, "assembleDebug")

        assertEquals(
            TaskOutcome.SUCCESS,
            result.task(":assembleDebug")?.outcome
        )

        assertFalse(!result.output.contains("ORTHOS"))
    }
}