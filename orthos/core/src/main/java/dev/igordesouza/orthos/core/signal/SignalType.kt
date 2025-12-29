package dev.igordesouza.orthos.core.signal

/**
 * Logical grouping for signals.
 *
 * Used for:
 * - Feature flag toggling
 * - Weight/severity presets
 * - Operational grouping
 */
enum class SignalType {

    /**
     * Detects execution environment anomalies.
     */
    ENVIRONMENT,

    /**
     * Detects app identity violations.
     */
    IDENTITY,

    /**
     * Detects code or binary tampering.
     */
    TAMPERING,

    /**
     * Detects runtime manipulation or hooking.
     */
    RUNTIME
}
