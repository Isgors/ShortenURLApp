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

                    # Keep injected canary method
                    -keepclassmembers class dev.igordesouza.** {
                        long __orthos_canary_value();
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
//
//
//package dev.igordesouza.orthos.plugin.task
//
//import org.gradle.api.DefaultTask
//import org.gradle.api.file.RegularFileProperty
//import org.gradle.api.tasks.InputFile
//import org.gradle.api.tasks.Optional
//import org.gradle.api.tasks.OutputFile
//import org.gradle.api.tasks.TaskAction
//
///**
// * Generates Proguard / R8 rules required by Orthos.
// *
// * This task is responsible for:
// * - Emitting keep rules for classes registered via OrthosKeepRegistry
// *   (bytecode canary holders, generated providers, etc.)
// * - Emitting hardcoded keep rules for critical runtime behavior
// *   (JNI bridges, runtime core, signals)
// */
//abstract class GenerateOrthosProguardRulesTask : DefaultTask() {
//
//
//    /**
//     * File produced by ASM visitors containing classes to keep.
//     */
//    @get:InputFile
//    @get:Optional
//    abstract val keepRegistryFile: RegularFileProperty
//
//    /**
//     * Generated Proguard rules file.
//     */
//    @get:OutputFile
//    abstract val outputFile: RegularFileProperty
//
//    @TaskAction
//    fun generate() {
//        val file = outputFile.get().asFile
//        file.parentFile.mkdirs()
//
//
//        val registryFile =
//            keepRegistryFile.orNull?.asFile
//                ?.takeIf { it.exists() }
//                ?.readLines()
//                ?.filter { it.isNotBlank() }
//                ?: emptyList()
//
//
//        val dynamicRules = if (registryFile != null && registryFile.exists()) {
//            registryFile.readLines()
//                .distinct()
//                .joinToString("\n") { className ->
//                    """
//                    -keep class $className { *; }
//                    -dontwarn $className
//                    """.trimIndent()
//                }
//        } else {
//            "# No Orthos classes registered"
//        }
//
//        val content = buildString {
//
//            appendLine(
//                """
//                # ==========================================
//                # Orthos Runtime – Auto-generated Proguard
//                # ==========================================
//                """.trimIndent()
//            )
//            appendLine()
//
//            // ------------------------------------------------------------
//            // Dynamic rules (registry-driven)
//            // ------------------------------------------------------------
//            appendLine("# Keep registry-driven generated classes")
//            appendLine(dynamicRules)
//            appendLine()
//
//            // ------------------------------------------------------------
//            // Static rules
//            // ------------------------------------------------------------
//
////            appendLine("# Keep injected canary method")
////            appendLine(
////                """
////                -keepclassmembers class ** {
////                    long __orthos_canary_value();
////                }
////                """.trimIndent()
////            )
////            appendLine()
//
//            appendLine("# Keep native agreement JNI bridge")
//            appendLine(
//                """
//                -keepclasseswithmembers class ** {
//                    native boolean nativeAgreement();
//                }
//                """.trimIndent()
//            )
//            appendLine()
//
//            appendLine("# Prevent aggressive shrinking of Orthos runtime")
//            appendLine(
//                """
//                -keep class dev.igordesouza.orthos.** { *; }
//                """.trimIndent()
//            )
//            appendLine()
//
//            appendLine("# Keep signals (reflection-safe)")
//            appendLine(
//                """
//                -keep class **Signal { *; }
//                """.trimIndent()
//            )
//            appendLine()
//
//            appendLine("# ==========================================")
//        }
//
//        file.writeText(content)
//    }
//}