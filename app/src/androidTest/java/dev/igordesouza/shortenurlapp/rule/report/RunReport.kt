package dev.igordesouza.shortenurlapp.rule.report

import dev.igordesouza.shortenurlapp.rule.model.TestMetric
import kotlinx.serialization.Serializable

@Serializable
data class RunReport(
    val suite: String,
    val totalTests: Int,
    val passed: Int,
    val failed: Int,
    val slaViolations: Int,
    val flakyTests: List<String>,
    val averageDurationMs: Long,
    val metrics: List<TestMetric>
)
