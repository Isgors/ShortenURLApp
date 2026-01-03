package dev.igordesouza.orthos.plugin.visitor

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Internal

/**
 * Parameters passed to Orthos ASM visitors.
 *
 * All parameters must be annotated to satisfy Gradle's
 * incremental and cache validation.
 *
 * This is the safest way to share data between:
 * - bytecode instrumentation workers
 * - Gradle tasks
 */
abstract class OrthosInstrumentationParams : InstrumentationParameters {

    /**
     * File-based registry used to collect classes
     * that require Proguard keep rules.
     *
     * This is a coordination side-channel and MUST NOT
     * participate in up-to-date or cache checks.
     */
    @get:Internal
    abstract val keepRegistryFile: RegularFileProperty
}