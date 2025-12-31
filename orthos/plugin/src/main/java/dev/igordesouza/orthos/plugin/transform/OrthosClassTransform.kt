package dev.igordesouza.orthos.plugin.transform

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import dev.igordesouza.orthos.plugin.extension.OrthosPluginExtension
import dev.igordesouza.orthos.plugin.generator.OrthosSignalsGenerator
import dev.igordesouza.orthos.plugin.signature.SignatureResolver
import dev.igordesouza.orthos.plugin.visitor.OrthosAsmClassVisitorFactory
import org.gradle.api.Project
import java.io.File

object OrthosClassTransform {

    fun register(project: Project, extension: OrthosPluginExtension) {

        if(extension.enabled) {
            project.tasks.register("generateOrthosSignals") {
                // 1️⃣ Generate OrthosSignals
                val generatedDir = File(
                    project.buildDir,
                    "generated/orthos/"
                )

                val output = File(
                    generatedDir,
                    "dev/igordesouza/orthos/runtime/generated/OrthosSignals.kt"
                )

                output.parentFile.mkdirs()
                output.writeText(OrthosSignalsGenerator.generate())

                val file = File(
                    generatedDir,
                    "dev/orthos/runtime/generated/OrthosSignals.kt"
                )

                file.parentFile.mkdirs()
                file.writeText(
                    OrthosSignalsGenerator.generate()
                )

            }
        }

        project.extensions.getByType(
            AndroidComponentsExtension::class.java
        ).onVariants { variant ->


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

            // 4️⃣ Register ASM instrumentation
            variant.instrumentation.transformClassesWith(
                OrthosAsmClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
            }

            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COPY_FRAMES
            )
        }
    }
}
