package dev.igordesouza.orthos.runtime.policy

import kotlinx.serialization.Serializable

@Serializable
data class Policy(
    val type: PolicyType,
    val suspiciousThreshold: Int? = null,
    val tamperedThreshold: Int? = null
)

@Serializable
enum class PolicyType {
    STRICT,
    GRADED
}
