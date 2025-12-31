package dev.igordesouza.orthos.plugin

import dev.igordesouza.orthos.plugin.extension.OrthosPluginExtension
import dev.igordesouza.orthos.plugin.transform.OrthosClassTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Entry point of the Orthos Gradle Plugin.
 *
 * Responsibilities:
 * - Register bytecode transformation
 * - Register source generation
 */
class OrthosRuntimePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create(
            "orthos",
            OrthosPluginExtension::class.java
        )

        project.afterEvaluate {
            if (extension.enabled) {
                OrthosClassTransform.register(
                    project = project,
                    extension = extension
                )
            }
        }
    }
}
