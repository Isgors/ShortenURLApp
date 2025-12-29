package dev.igordesouza.orthos.core.verdict

/**
 * Final evaluated state of the runtime.
 */
enum class RuntimeState {
    SAFE,
    SUSPICIOUS,
    TAMPERED
}
