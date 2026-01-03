package dev.igordesouza.orthos.plugin

/**
 * Public configuration surface for the Orthos plugin.
 */
open class OrthosPluginExtension {

    /**
     * Enables or disables bytecode instrumentation.
     */
    var enabled: Boolean = true

    /**
     * Optional override of enabled signals.
     */
    var enabledBuildTypes: Set<String> = setOf()
}