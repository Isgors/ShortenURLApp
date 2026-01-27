package dev.igordesouza.shortenurlapp.rule.retry

/**
 * Central retry policy.
 *
 * Defines:
 * - Maximum retries per category
 * - Whether retries are allowed at all
 *
 * IMPORTANT:
 * ReleaseGate tests are intentionally strict.
 */
object RetryPolicy {

    fun maxRetries(categories: List<String>): Int =
        when {
            "ReleaseGate" in categories -> 0
            "SystemUi" in categories -> 2
            "Flaky" in categories -> 3
            else -> 1
        }

    fun isRetryAllowed(categories: List<String>): Boolean =
        maxRetries(categories) > 0
}
