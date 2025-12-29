package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.OrthosVerdict.Companion.tampered

/**
 * Conservative fail-safe:
 * Any failure results in a tampered verdict.
 */
class ConservativeFailSafeHandler : FailSafeHandler {

    override fun onFailure(throwable: Throwable): OrthosVerdict {
        return OrthosVerdict.tampered(
            scoreBoost = Int.MAX_VALUE,
            results = emptyList()
        )
    }
}
