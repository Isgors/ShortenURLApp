package dev.igordesouza.orthos.runtime.policy.dsl

import dev.igordesouza.orthos.core.verdict.RuntimeState

/**
 * Single decision rule based on score threshold.
 */
data class PolicyRule(
    val minScore: Int,
    val state: RuntimeState
)
