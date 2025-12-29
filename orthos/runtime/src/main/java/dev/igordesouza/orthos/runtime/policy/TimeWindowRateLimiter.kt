package dev.igordesouza.orthos.runtime.policy

/**
 * Simple time-window rate limiter.
 */
class TimeWindowRateLimiter(
    private val windowMillis: Long
) : RateLimiter {

    private var lastExecutionTime: Long = 0L

    override fun allowExecution(): Boolean {
        val now = System.currentTimeMillis()
        return if (now - lastExecutionTime > windowMillis) {
            lastExecutionTime = now
            true
        } else {
            false
        }
    }
}
