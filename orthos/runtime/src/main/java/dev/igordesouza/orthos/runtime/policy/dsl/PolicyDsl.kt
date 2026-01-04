package dev.igordesouza.orthos.runtime.policy.dsl

class PolicyDsl {

    private var scoreStrategy: ScoreStrategy = SumScoreStrategy
    private val rules = mutableListOf<PolicyRule>()

    fun score(strategy: ScoreStrategy) {
        scoreStrategy = strategy
    }

    fun whenScore(block: WhenScoreDsl.() -> Unit) {
        rules += WhenScoreDsl().apply(block).build()
    }

    fun build(): PolicyDefinition =
        PolicyDefinition(
            scoreStrategy = scoreStrategy,
            rules = rules.sortedByDescending { it.minScore }
        )
}


fun policy(block: PolicyDsl.() -> Unit): PolicyDefinition =
    PolicyDsl().apply(block).build()
