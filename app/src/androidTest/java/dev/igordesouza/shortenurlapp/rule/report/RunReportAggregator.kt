package dev.igordesouza.shortenurlapp.rule.report

import dev.igordesouza.shortenurlapp.rule.model.TestMetric
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Aggregates per-test metric JSONs into a single run-level report.
 */
object RunReportAggregator {

    private val json = Json { ignoreUnknownKeys = true }

    fun aggregate(inputDir: File, outputFile: File) {
        val metrics = inputDir
            .listFiles { f -> f.extension == "json" }
            ?.map { json.decodeFromString<TestMetric>(it.readText()) }
            ?: emptyList()

        val flakyTests =
            metrics
                .groupBy { "${it.className}#${it.testName}" }
                .filterValues { attempts ->
                    attempts.any { it.status == TestMetric.Status.FAIL } &&
                        attempts.any { it.status == TestMetric.Status.PASS }
                }
                .keys
                .toList()

        val report = RunReport(
            suite = metrics.firstOrNull()?.suite ?: "unknown",
            totalTests = metrics.map { it.testName }.distinct().count(),
            passed = metrics.count { it.status == TestMetric.Status.PASS },
            failed = metrics.count { it.status == TestMetric.Status.FAIL },
            slaViolations = metrics.count { it.status == TestMetric.Status.SLA_VIOLATION },
            flakyTests = flakyTests,
            averageDurationMs =
                metrics.map { it.durationMs }.average().toLong(),
            metrics = metrics
        )

        outputFile.writeText(json.encodeToString(report))
    }
}
