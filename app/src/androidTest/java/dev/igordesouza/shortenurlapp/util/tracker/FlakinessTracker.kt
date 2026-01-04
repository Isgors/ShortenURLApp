package dev.igordesouza.shortenurlapp.util.tracker

import java.util.concurrent.atomic.AtomicInteger

object FlakinessTracker {

    private val retryCount = AtomicInteger(0)

    fun recordRetry() {
        retryCount.incrementAndGet()
    }

    fun reset() {
        retryCount.set(0)
    }

    fun report(testName: String) {
        val retries = retryCount.get()
        if (retries > 0) {
            println("⚠️ Flakiness detected in $testName: $retries retries")
        }
    }
}