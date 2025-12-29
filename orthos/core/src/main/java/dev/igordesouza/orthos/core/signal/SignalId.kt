package dev.igordesouza.orthos.core.signal

/**
 * Canonical identifiers for all supported signals.
 *
 * - Must be immutable once released.
 * - Used for reporting, remote configuration and analytics.
 * - Each SignalId must have exactly one concrete implementation.
 */
enum class SignalId {
    EMULATOR,
    VIRTUALIZATION,
    CLONE_APP,
    SIGNATURE,
    SELF_HASH,
    BYTECODE_CANARY,
    NATIVE_AGREEMENT,
    ROOT,

    UNKNOWN
}
