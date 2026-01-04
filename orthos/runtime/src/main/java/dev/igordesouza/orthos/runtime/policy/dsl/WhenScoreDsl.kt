package dev.igordesouza.orthos.runtime.policy.dsl

import dev.igordesouza.orthos.core.verdict.RuntimeState

class WhenScoreDsl {

    private val rules = mutableListOf<PolicyRule>()
    private var otherwiseState: RuntimeState? = null

    fun atLeast(score: Int, state: RuntimeState) {
        rules += PolicyRule(score, state)
    }

    val otherwise: OtherwiseDsl
        get() = OtherwiseDsl { state ->
            otherwiseState = state
        }

    fun build(): List<PolicyRule> {
        val fallback = otherwiseState
            ?: error("Policy must define an otherwise state")

        return rules + PolicyRule(Int.MIN_VALUE, fallback)
    }
}

class OtherwiseDsl(
    private val setter: (RuntimeState) -> Unit
) {
    infix fun then (state: RuntimeState) {
        setter(state)
    }
}
