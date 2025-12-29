package dev.igordesouza.orthos.core.signal

/**
 * Result produced by a Signal execution.
 *
 * @property signalId Unique identifier of the signal.
 * @property triggered Whether the signal detected an anomaly.
 * @property confidence Confidence level in the detection [0.0 - 1.0].
 * @property metadata Optional diagnostic data.
 */
data class SignalResult(
    val signalId: SignalId,
    val triggered: Boolean,
    val confidence: Float,
    val metadata: Map<String, String> = emptyMap()
)
