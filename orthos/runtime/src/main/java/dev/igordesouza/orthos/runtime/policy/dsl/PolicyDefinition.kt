package dev.igordesouza.orthos.runtime.policy.dsl

/**
 * Fully resolved policy definition.
 */
data class PolicyDefinition(
    val scoreStrategy: ScoreStrategy,
    val rules: List<PolicyRule>
)
