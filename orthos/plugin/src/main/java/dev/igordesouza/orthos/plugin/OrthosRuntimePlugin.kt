package dev.igordesouza.orthos.plugin

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import dev.igordesouza.orthos.plugin.proguard.OrthosProguardRules
import dev.igordesouza.orthos.plugin.task.PrepareOrthosKeepRegistryTask
import dev.igordesouza.orthos.plugin.visitor.OrthosAsmClassVisitorFactory
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

        val extension = project.extensions.create("orthos", OrthosPluginExtension::class.java)
        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)

        androidComponents?.onVariants { variant ->
            if (!extension.enabled) return@onVariants

            val currentBuildType = variant.buildType ?: ""

            if (currentBuildType in extension.enabledBuildTypes) {
                project.logger.lifecycle("Orthos ENABLED for variant: ${variant.name} (BuildType: $currentBuildType)")


                /*
                 * Prepare a file to collect all visited class names
                 * so the Proguard task can later generate keep rules.
                 * This must be created before instrumentation runs.
                 */
                val keepRegistryFile =
                    project.layout.buildDirectory.file(
                        "generated/orthos/${variant.name}/keep-registry.txt"
                    )

                // Ensure the file exists early so it is a valid @InputFile
                val prepareRegistry = project.tasks.register(
                    "prepareOrthosKeepRegistry${variant.name.replaceFirstChar { it.uppercase() }}",
                    PrepareOrthosKeepRegistryTask::class.java
                ) { task ->
                    task.outputFile.set(keepRegistryFile)
                }



                // Register ASM instrumentation
                variant.instrumentation.transformClassesWith(
                    OrthosAsmClassVisitorFactory::class.java,
                    InstrumentationScope.ALL
                ) { params ->
                    params.keepRegistryFile.set(keepRegistryFile)
                }

                variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COPY_FRAMES
                )

                if (variant.buildType == "release") {
                    OrthosProguardRules.register(
                        project = project,
                        variant = variant,
                        keepRegistryFile = keepRegistryFile,
                        prepareRegistry = prepareRegistry
                    )
                }

            } else {
                project.logger.lifecycle("Orthos DISABLED for variant: ${variant.name} (BuildType: $currentBuildType is not in ${extension.enabledBuildTypes})")
            }
        }
    }
}