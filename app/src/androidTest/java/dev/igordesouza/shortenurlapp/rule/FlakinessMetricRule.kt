package dev.igordesouza.shortenurlapp.rule

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import dev.igordesouza.shortenurlapp.rule.model.TestMetric
import org.junit.experimental.categories.Category
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Collects execution metrics, enforces SLAs, and exports results as JSON.
 *
 * Responsibilities:
 * - Measure execution time
 * - Detect pass/fail
 * - Enforce category-based SLA
 * - Export metrics for CI aggregation
 */
class FlakinessMetricRule(
    private val suite: String
) : TestRule {

    private val gson = Gson()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {
                var failure: Throwable? = null
                val startTimestamp = System.currentTimeMillis()

                val durationMs = measureTimeMillis {
                    try {
                        base.evaluate()
                    } catch (t: Throwable) {
                        failure = t
                        throw t
                    }
                }

                val categories = extractCategories(description)
                val retries = extractRetries()

                val status = when {
                    violatesSla(durationMs, categories) ->
                        TestMetric.Status.SLA_VIOLATION
                    failure == null ->
                        TestMetric.Status.PASS
                    else ->
                        TestMetric.Status.FAIL
                }

                val metric = TestMetric(
                    suite = suite,
                    testName = description.methodName,
                    status = status,
                    durationMs = durationMs,
                    retries = retries,
                    categories = categories,
                    deviceModel = Build.MODEL ?: "unknown",
                    apiLevel = Build.VERSION.SDK_INT,
                    timestamp = startTimestamp
                )

                exportMetric(metric)

                RetryContext.clear()
                if (status == TestMetric.Status.SLA_VIOLATION) {
                    error(
                        "SLA violation: ${description.methodName} took ${durationMs}ms " +
                                "for categories=$categories"
                    )
                }
            }
        }
    }

    // ------------------ SLA ------------------

    private fun violatesSla(
        durationMs: Long,
        categories: List<String>
    ): Boolean {
        val maxAllowed = when {
            categories.contains("ReleaseGate") -> 3_000
            categories.contains("SystemUi") -> 10_000
            else -> 15_000
        }
        return durationMs > maxAllowed
    }

    // ------------------ METADATA ------------------

    private fun extractCategories(description: Description): List<String> {
        return description.annotations
            .filterIsInstance<Category>()
            .flatMap { category ->
                category.value.mapNotNull { clazz ->
                    clazz.simpleName
                }
            }
    }

    private fun extractRetries(): Int {
        return RetryContext.getRetries()
    }

    // ------------------ EXPORT ------------------

    private fun exportMetric(metric: TestMetric) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(context.filesDir, "test-metrics.json")

        file.appendText(gson.toJson(metric) + "\n")
    }
}
