package dev.igordesouza.orthos.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Prepares the Orthos keep-registry file before bytecode instrumentation runs.
 *
 * Why this task exists:
 * - Gradle validates @InputFile / @OutputFile existence at configuration time
 * - ASM visitors append class names during instrumentation
 * - Without this task, the registry file would not exist yet
 *
 * This task guarantees:
 * - The file exists before any visitor tries to append
 * - The file is a valid Gradle output
 * - Parallel workers can safely append to it
 */
abstract class PrepareOrthosKeepRegistryTask : DefaultTask() {

    /**
     * File used as a registry where ASM visitors will append
     * fully-qualified class names that require Proguard keep rules.
     */
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun prepare() {
        val file = outputFile.get().asFile

        // Ensure directory exists
        file.parentFile.mkdirs()

        // Create or truncate the file
        if (!file.exists()) {
            file.createNewFile()
        } else {
            // Important: truncate on every build to avoid stale entries
            file.writeText("")
        }

        logger.debug(
            "Orthos: prepared keep-registry file at ${file.absolutePath}"
        )
    }
}
