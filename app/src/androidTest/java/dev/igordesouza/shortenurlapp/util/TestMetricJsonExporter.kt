package dev.igordesouza.shortenurlapp.util

import dev.igordesouza.shortenurlapp.rule.model.TestMetric
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Serializes [TestMetric] objects to JSON files using kotlinx.serialization.
 *
 * Output is both:
 * - human-readable (pretty printed)
 * - machine-readable (stable schema)
 */
object TestMetricJsonExporter {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    fun export(
        outputDir: File,
        metric: TestMetric
    ) {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val file = File(
            outputDir,
            "${metric.className}_${metric.testName}_attempt${metric.attempt}.json"
        )

        file.writeText(json.encodeToString(metric))
    }
}
