package dev.igordesouza.shortenurlapp.rule

import org.junit.experimental.categories.Category
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Retries flaky tests only.
 *
 * Retry count is propagated via [RetryContext].
 */
class RetryFlakyRule(
    private val maxRetries: Int = 2
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {
                val isFlaky = description.annotations
                    .filterIsInstance<Category>()
                    .any { category ->
                        category.value.any { it.simpleName == "Flaky" }
                    }

                val allowedRetries = if (isFlaky) maxRetries else 0
                var attempt = 0
                var lastError: Throwable? = null

                try {
                    while (attempt <= allowedRetries) {
                        try {
                            base.evaluate()
                            RetryContext.setRetries(attempt)
                            return
                        } catch (t: Throwable) {
                            lastError = t
                            attempt++
                        }
                    }
                } finally {
                    // Ensures clean state between tests
                    RetryContext.setRetries(attempt - 1)
                }

                throw lastError!!
            }
        }
    }
}
