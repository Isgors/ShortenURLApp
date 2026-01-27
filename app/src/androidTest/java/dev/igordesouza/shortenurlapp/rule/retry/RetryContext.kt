package dev.igordesouza.shortenurlapp.rule.retry

/**
 * Thread-local execution context for retry-aware test infrastructure.
 *
 * This context exists to share retry-related runtime information between:
 * - RetryFlakyRule
 * - FlakinessMetricRule
 * - SLA enforcement
 *
 * IMPORTANT:
 * - JUnit 4 does not allow mutation of test metadata at runtime
 * - This is the only safe communication channel between rules
 *
 * Guarantees:
 * - Thread-safe (ThreadLocal)
 * - Shard-safe
 * - Compatible with Android Test Orchestrator
 */
object RetryContext {

    private val attempt = ThreadLocal.withInitial { 1 }
    private val lastRunPassed = ThreadLocal.withInitial { false }
    private val lastRetryReason = ThreadLocal<RetryReason?>()

    /**
     * Called by RetryFlakyRule before each test execution attempt.
     */
    fun startAttempt(attemptNumber: Int) {
        attempt.set(attemptNumber)
        lastRunPassed.set(false)
        lastRetryReason.set(null)
    }

    /**
     * Marks the current attempt as successful.
     *
     * Should be called only if the test body completes without throwing.
     */
    fun markPassed() {
        lastRunPassed.set(true)
    }

    /**
     * Marks the current attempt as failed.
     *
     * Should be called when an exception is thrown.
     */
    fun markFailed(reason: RetryReason) {
        lastRunPassed.set(false)
        lastRetryReason.set(reason)
    }

    /**
     * @return the current retry attempt number (1-based).
     */
    fun currentAttempt(): Int = attempt.get()

    /**
     * @return true if the last attempt completed successfully.
     */
    fun lastRunPassed(): Boolean = lastRunPassed.get()

    fun lastRetryReason(): RetryReason? = lastRetryReason.get()

    /**
     * Clears thread-local state.
     *
     * MUST be called at the end of test execution to avoid
     * state leakage between tests.
     */
    fun clear() {
        attempt.remove()
        lastRunPassed.remove()
        lastRetryReason.remove()
    }

}