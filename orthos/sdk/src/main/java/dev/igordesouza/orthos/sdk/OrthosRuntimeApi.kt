package dev.igordesouza.orthos.sdk

import dev.igordesouza.orthos.core.verdict.OrthosVerdict

/**
 * Public runtime contract used by the consuming app.
 *
 * We keep this interface in the SDK so the app can swap between:
 * - the real runtime implementation (wrapped)
 * - a [NoOpOrthosRuntime]
 */
interface OrthosRuntimeApi {
    /** Runs Orthos evaluation and returns a verdict. */
    fun evaluate(): OrthosVerdict
}
