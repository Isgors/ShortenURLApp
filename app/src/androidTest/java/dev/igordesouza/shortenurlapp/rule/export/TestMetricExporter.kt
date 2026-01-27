package dev.igordesouza.shortenurlapp.rule.export

import dev.igordesouza.shortenurlapp.rule.model.TestMetric
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Writes each TestMetric as an individual JSON file.
 *
 * This avoids:
 * - race conditions
 * - shard collisions
 * - parallel execution corruption
 */
object TestMetricExporter {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun export(outputDir: File, metric: TestMetric) {
        if (!outputDir.exists()) outputDir.mkdirs()

        val fileName =
            "${metric.className}_${metric.testName}_attempt${metric.attempt}.json"

        File(outputDir, fileName)
            .writeText(json.encodeToString(metric))
    }
}
