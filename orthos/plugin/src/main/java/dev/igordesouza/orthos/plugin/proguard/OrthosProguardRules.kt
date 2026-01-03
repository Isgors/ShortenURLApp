package dev.igordesouza.orthos.plugin.proguard

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.Variant
import dev.igordesouza.orthos.plugin.task.GenerateOrthosProguardRulesTask
import dev.igordesouza.orthos.plugin.task.PrepareOrthosKeepRegistryTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider

object OrthosProguardRules {

    fun register(
        project: Project,
        variant: Variant,
        keepRegistryFile: Provider<RegularFile>,
        prepareRegistry: TaskProvider<PrepareOrthosKeepRegistryTask>,
    ) {

        val capitalized = variant.name.replaceFirstChar { it.uppercase() }

        val taskProvider = project.tasks.register(
            "generateOrthosProguardRules$capitalized",
            GenerateOrthosProguardRulesTask::class.java
        ) { task ->

            task.keepRegistryFile.set(keepRegistryFile)

            task.outputFile.set(
                project.layout.buildDirectory.file(
                    "generated/orthos/${variant.name}/orthos-rules.pro"
                )
            )
        }

        taskProvider.configure {
            it.dependsOn(prepareRegistry)
        }

        // Ensure R8 depends on it
        project.tasks.matching {
            it.name == "minify${capitalized}WithR8"
        }.configureEach {
            it.dependsOn(taskProvider)
        }

        variant.proguardFiles.add(
            taskProvider.flatMap { it.outputFile }
        )

        // ---------- APPLICATION ----------
        project.plugins.withId("com.android.application") {

            val android = project.extensions
                .getByType(ApplicationExtension::class.java)

            android.buildTypes.configureEach { buildType ->
                if (buildType.name == "release") {
                    buildType.proguardFiles(
                        taskProvider.flatMap { it.outputFile }
                    )
                }
            }
        }

        // ---------- LIBRARY ----------
        project.plugins.withId("com.android.library") {

            val android = project.extensions
                .getByType(LibraryExtension::class.java)

            android.defaultConfig {
                consumerProguardFiles(
                    taskProvider.flatMap { it.outputFile }
                )
            }
        }
    }
}
