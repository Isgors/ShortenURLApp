package dev.igordesouza.shortenurlapp.util

import dev.igordesouza.shortenurlapp.util.tracker.FlakinessTracker

inline fun retry(
    times: Int = 3,
    delayMillis: Long = 1_000,
    block: () -> Boolean
): Boolean {
    repeat(times - 1) {
        if (block()) return true
        FlakinessTracker.recordRetry()
        Thread.sleep(delayMillis)
    }
    return block()
}
