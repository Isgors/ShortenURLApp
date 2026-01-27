package dev.igordesouza.shortenurlapp.rule

import dev.igordesouza.shortenurlapp.category.extractCategories
import dev.igordesouza.shortenurlapp.rule.retry.RetryContext
import dev.igordesouza.shortenurlapp.rule.retry.RetryPolicy
import dev.igordesouza.shortenurlapp.rule.retry.RetryReasonClassifier
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Retries flaky tests based on category-driven retry policy.
 *
 * Responsibilities:
 * - Control retry attempts
 * - Track retry reasons
 * - Communicate state via RetryContext
 *
 * This rule:
 * - NEVER retries ReleaseGate tests
 * - Is safe for sharding & parallel execution
 * - Does not swallow failures
 */
class RetryFlakyRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {

            override fun evaluate() {
                val categories = description.extractCategories()
                val maxRetries = RetryPolicy.maxRetries(categories)

                var lastFailure: Throwable? = null

                for (attempt in 1..(maxRetries + 1)) {
                    RetryContext.startAttempt(attempt)

                    try {
                        base.evaluate()
                        RetryContext.markPassed()
                        return
                    } catch (t: Throwable) {
                        val reason = RetryReasonClassifier.classify(t)
                        RetryContext.markFailed(reason)
                        lastFailure = t

                        val isLastAttempt = attempt > maxRetries
                        if (isLastAttempt) break
                    }
                }

                RetryContext.clear()
                throw lastFailure ?: error("Retry failed without exception")
            }
        }
}
