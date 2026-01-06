package dev.igordesouza.shortenurlapp.rule.annotation

/**
 * Internal annotation used to communicate retry count
 * from RetryFlakyRule to FlakinessMetricRule.
 *
 * This is NOT meant to be written manually on tests.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RetryCount(val count: Int)