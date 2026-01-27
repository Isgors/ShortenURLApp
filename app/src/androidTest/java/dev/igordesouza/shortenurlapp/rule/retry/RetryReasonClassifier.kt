package dev.igordesouza.shortenurlapp.rule.retry

import java.util.concurrent.TimeoutException
import junit.framework.AssertionFailedError

/**
 * Framework-agnostic failure classification.
 *
 * Designed to work with:
 * - Compose UI tests
 * - UiAutomator
 * - JVM tests
 * - Future frameworks
 */
object RetryReasonClassifier {

    fun classify(t: Throwable): RetryReason =
        when {
            isAssertionFailure(t) ->
                RetryReason.ASSERTION_FAILURE

            isTimeout(t) ->
                RetryReason.TIMEOUT

            isUiSyncIssue(t) ->
                RetryReason.UI_SYNC

            else ->
                RetryReason.UNKNOWN
        }

    private fun isAssertionFailure(t: Throwable): Boolean =
        generateSequence(t) { it.cause }
            .any { it is AssertionError ||  t.cause is AssertionError }

    private fun isTimeout(t: Throwable): Boolean =
        t is TimeoutException ||
                t.cause is TimeoutException

    /**
     * Heuristic for UI synchronization issues.
     *
     * Covers:
     * - Compose recomposition delays
     * - UiAutomator waits
     * - Race conditions
     */
    private fun isUiSyncIssue(t: Throwable): Boolean =
        t is IllegalStateException &&
                t.message?.contains("waiting", ignoreCase = true) == true
}
