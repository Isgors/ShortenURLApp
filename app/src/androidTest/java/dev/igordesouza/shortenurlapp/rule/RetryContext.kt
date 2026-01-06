package dev.igordesouza.shortenurlapp.rule

/**
 * Thread-local storage for retry metadata.
 *
 * Needed because JUnit 4 does not allow
 * mutating test Descriptions at runtime.
 */
internal object RetryContext {

    private val retryCount = ThreadLocal<Int>()

    fun setRetries(count: Int) {
        retryCount.set(count)
    }

    fun getRetries(): Int = retryCount.get() ?: 0

    fun clear() {
        retryCount.remove()
    }
}