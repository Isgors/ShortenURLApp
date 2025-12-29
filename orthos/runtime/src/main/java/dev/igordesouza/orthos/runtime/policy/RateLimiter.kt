package dev.igordesouza.orthos.runtime.policy

/**
 * Controls how often policy evaluation can occur.
 *
 * Used to mitigate:
 * - Timing attacks
 * - Excessive execution
 * - Signal brute forcing
 */
interface RateLimiter {

    /**
     * @return true if execution is allowed
     */
    fun allowExecution(): Boolean
}
