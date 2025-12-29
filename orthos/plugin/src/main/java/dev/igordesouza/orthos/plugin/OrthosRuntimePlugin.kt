package dev.igordesouza.orthos.plugin

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin Gradle responsável por registrar o pipeline de
 * instrumentação ASM do Orthos.
 */
class OrthosRuntimePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create(
            "orthos",
            OrthosPluginExtension::class.java
        )

        project.extensions.getByType(AndroidComponentsExtension::class.java)
            .onVariants { variant ->

                if (!extension.enabled) return@onVariants

                val buildType = variant.buildType ?: return@onVariants

                if (buildType !in extension.enabledBuildTypes) {
                    project.logger.lifecycle(
                        "Orthos disabled for variant ${variant.name}"
                    )
                    return@onVariants
                }

                project.logger.lifecycle(
                    "Orthos enabled for variant ${variant.name}"
                )

                variant.instrumentation.transformClassesWith(
                    OrthosClassTransformer::class.java,
                    InstrumentationScope.ALL
                ) {  }

                variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COPY_FRAMES
                )
            }
    }
}
