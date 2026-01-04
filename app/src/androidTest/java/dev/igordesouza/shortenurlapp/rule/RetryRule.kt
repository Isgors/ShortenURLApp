package dev.igordesouza.shortenurlapp.rule

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RetryRule(
    private val retryCount: Int = 2
) : TestRule {

    override fun apply(base: Statement, description: Description) =
        object : Statement() {
            override fun evaluate() {
                var lastThrowable: Throwable? = null
                repeat(retryCount + 1) {
                    try {
                        base.evaluate()
                        return
                    } catch (t: Throwable) {
                        lastThrowable = t
                    }
                }
                throw lastThrowable!!
            }
        }
}
