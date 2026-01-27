package dev.igordesouza.shortenurlapp.rule.sla

/**
 * Centralized SLA definitions per category.
 *
 * Keeping this isolated allows:
 * - CI overrides
 * - Feature-flagging
 * - Per-device tuning
 */
object SlaPolicy {

    fun maxAllowedMs(categories: List<String>): Long =
        when {
            "ReleaseGate" in categories -> 3_000
            "SystemUi" in categories -> 10_000
            "Flaky" in categories -> 15_000
            else -> 20_000
        }
}
