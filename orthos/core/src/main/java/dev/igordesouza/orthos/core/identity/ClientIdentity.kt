package dev.igordesouza.orthos.core.identity

import kotlinx.serialization.Serializable

/**
 * Represents a stable and deterministic identity used
 * to evaluate feature flags and detection policies.
 *
 * This identity is immutable during runtime.
 */
@Serializable
data class ClientIdentity(
    val appId: String,
    val installId: String,
    val userId: String? = null
)