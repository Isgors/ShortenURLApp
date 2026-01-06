package dev.igordesouza.shortenurlapp.rule.model

/**
 * Immutable representation of a single test execution.
 *
 * This model is designed to be:
 * - Machine-readable (JSON)
 * - Human-readable (CI rendering)
 * - Stable across versions
 */
data class TestMetric(
    val suite: String,
    val testName: String,
    val status: Status,
    val durationMs: Long,
    val retries: Int,
    val categories: List<String>,
    val deviceModel: String,
    val apiLevel: Int,
    val timestamp: Long
) {
    enum class Status {
        PASS, FAIL, SLA_VIOLATION
    }
}
