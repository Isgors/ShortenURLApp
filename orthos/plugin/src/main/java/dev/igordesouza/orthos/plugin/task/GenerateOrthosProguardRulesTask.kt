package dev.igordesouza.orthos.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

/**
 * Generates Proguard rules required by Orthos bytecode instrumentation.
 *
 * Reads the keep-registry generated during ASM instrumentation.
 */
abstract class GenerateOrthosProguardRulesTask : DefaultTask() {

    /**
     * Registry file populated by ASM visitors.
     *
     * Must be OPTIONAL because:
     * - It does not exist at configuration time
     * - It is created by another task
     */
    @get:InputFile
    @get:Optional
    abstract val keepRegistryFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val outFile = outputFile.get().asFile
        outFile.parentFile.mkdirs()

        val registry =
            keepRegistryFile.orNull?.asFile
                ?.takeIf { it.exists() }
                ?.readLines()
                ?.filter { it.isNotBlank() }
                ?: emptyList()

        outFile.writeText(
            buildString {
                appendLine(
                    """
                    # ==========================================
                    # Orthos Runtime – Auto-generated Proguard
                    # ==========================================
                    
                    # Keep injected native canary method
                    -keepclassmembers class dev.igordesouza.** {
                        long __orthos_native_agreement();
                    }
                    
                    # Keep native agreement JNI bridge
                    -keepclasseswithmembers class dev.igordesouza.** {
                        native boolean nativeAgreement();
                    }
                    
                    # Keep Orthos runtime
                    -keep class dev.igordesouza.orthos.** { *; }

                    """.trimIndent()
                )

                if (registry.isNotEmpty()) {
                    appendLine()
                    appendLine("# ==========================================")
                    appendLine("# Classes discovered by Orthos ASM visitors")
                    appendLine("# ==========================================")

                    registry.forEach { className ->
                        appendLine("-keep class $className { *; }")
                        appendLine("-dontwarn $className")
                    }
                }
            }
        )
    }
}