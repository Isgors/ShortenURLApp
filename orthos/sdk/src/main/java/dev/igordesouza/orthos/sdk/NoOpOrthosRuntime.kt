package dev.igordesouza.orthos.sdk

import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.RuntimeState

/**
 * A safe no-op implementation used when Orthos is disabled for the current build variant.
 *
 * This ensures the consuming app can keep the same code path and DI graph while
 * guaranteeing Orthos does not execute any security logic.
 */
class NoOpOrthosRuntime : OrthosRuntimeApi {
    override fun evaluate(): OrthosVerdict = OrthosVerdict(
        state = RuntimeState.SAFE,
        score = 0,
        evidences = emptyList()
    )
}
