package dev.igordesouza.shortenurlapp.rule

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import dev.igordesouza.shortenurlapp.category.extractCategories
import dev.igordesouza.shortenurlapp.rule.export.TestMetricExporter
import dev.igordesouza.shortenurlapp.rule.model.TestMetric
import dev.igordesouza.shortenurlapp.rule.retry.RetryContext
import dev.igordesouza.shortenurlapp.rule.sla.SlaPolicy
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Measures execution metrics, enforces SLAs, and exports structured JSON results.
 *
 * This rule is:
 * - Retry-aware
 * - Shard-safe
 * - Device-aware
 * - CI-friendly
 *
 * ONE metric file is generated per test attempt.
 */
class FlakinessMetricRule(
    private val suite: String
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {

            override fun evaluate() {
                var failure: Throwable? = null
                val startTimestamp = System.currentTimeMillis()

                val durationMs = measureTimeMillis {
                    try {
                        base.evaluate()
                        RetryContext.markPassed()
                    } catch (t: Throwable) {
                        failure = t
                        throw t
                    }
                }

                val categories = description.extractCategories()
                val maxAllowedMs = SlaPolicy.maxAllowedMs(categories)

                val status = when {
                    durationMs > maxAllowedMs ->
                        TestMetric.Status.SLA_VIOLATION
                    failure == null ->
                        TestMetric.Status.PASS
                    else ->
                        TestMetric.Status.FAIL
                }

                val context =
                    InstrumentationRegistry.getInstrumentation().targetContext

                val outputDir = File(
                    context.getExternalFilesDir(null),
                    "test-metrics/$suite"
                )

                val metric = TestMetric(
                    suite = suite,
                    className = description.className,
                    testName = description.methodName,
                    categories = categories,
                    attempt = RetryContext.currentAttempt(),
                    retryReason = RetryContext.lastRetryReason()?.name,
                    status = status,
                    durationMs = durationMs,
                    deviceModel = Build.MODEL ?: "unknown",
                    apiLevel = Build.VERSION.SDK_INT,
                    timestampEpochMs = startTimestamp
                )

                TestMetricExporter.export(outputDir, metric)

                RetryContext.clear()

                if (status == TestMetric.Status.SLA_VIOLATION) {
                    error(
                        "SLA violation: ${description.methodName} " +
                                "took ${durationMs}ms (max=$maxAllowedMs ms)"
                    )
                }
            }
        }
}
