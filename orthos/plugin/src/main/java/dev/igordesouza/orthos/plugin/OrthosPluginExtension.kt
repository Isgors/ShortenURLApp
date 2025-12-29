package dev.igordesouza.orthos.plugin

/**
 * Extension do Gradle para configuração futura do Orthos.
 *
 * Mantida mínima propositalmente para reduzir superfície
 * de ataque e variabilidade de builds.
 */
open class OrthosPluginExtension {

    /**
     * Ativa ou desativa a instrumentação.
     * Default: true
     */
    var enabled: Boolean = true

    /**
     * Lista de buildTypes onde o Orthos deve rodar.
     *
     * Default: somente release.
     */
    var enabledBuildTypes: Set<String> = setOf("release")
}
