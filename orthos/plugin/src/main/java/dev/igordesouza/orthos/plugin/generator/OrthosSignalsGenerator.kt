package dev.igordesouza.orthos.plugin.generator

/**
 * Generates the OrthosSignals object, which provides
 * the immutable list of all runtime signals.
 *
 * This object is generated at build-time and compiled
 * into the app.
 */
object OrthosSignalsGenerator {

    fun generate(packageName: String): String {
        return """
            package {$packageName}

            import dev.igordesouza.orthos.runtime.signal.*
            import dev.igordesouza.orthos.runtime.signal.impl.*

            /**
             * Generated entry-point for all Orthos signals.
             *
             * This list is deterministic and immutable.
             */
            object OrthosSignals {

                fun generated(): List<Signal> = listOf(
                    BytecodeCanarySignal(),
                    NativeAgreementSignal(),
                    RootSignal(),
                    EmulatorSignal(),
                    VirtualizationSignal(),
                    CloneAppSignal()
                )
            }
        """.trimIndent()
    }
}