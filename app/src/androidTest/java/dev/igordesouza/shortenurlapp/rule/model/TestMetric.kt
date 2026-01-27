package dev.igordesouza.shortenurlapp.rule.model

import kotlinx.serialization.Serializable

/**
 * Immutable execution record for a single test attempt.
 * This model is designed to be:
 * - Machine-readable (JSON)
 * - Human-readable (CI rendering)
 * - Stable across versions
 * One instance == one execution attempt (including retries).
 */
@Serializable
data class TestMetric(
    val suite: String,
    val className: String,
    val testName: String,
    val categories: List<String>,
    val attempt: Int,
    val status: Status,
    val durationMs: Long,
    val deviceModel: String,
    val apiLevel: Int,
    val timestampEpochMs: Long,
    val retryReason: String?
) {
    enum class Status {
        PASS,
        FAIL,
        SLA_VIOLATION
    }
}
