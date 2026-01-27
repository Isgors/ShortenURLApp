package dev.igordesouza.shortenurlapp.rule.retry

/**
 * Canonical reasons for retrying a test.
 *
 * These are intentionally coarse-grained so they can be
 * aggregated and reasoned about in CI dashboards.
 */
enum class RetryReason {
    ASSERTION_FAILURE,
    TIMEOUT,
    UI_SYNC,
    SYSTEM_DIALOG,
    NETWORK,
    UNKNOWN
}
