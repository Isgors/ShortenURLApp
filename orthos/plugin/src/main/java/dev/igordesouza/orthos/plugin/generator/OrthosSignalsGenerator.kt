package dev.igordesouza.orthos.plugin.generator

/**
 * Generates the OrthosSignals object, which provides
 * the immutable list of all runtime signals.
 *
 * This object is generated at build-time and compiled
 * into the app.
 */
object OrthosSignalsGenerator {

    fun generate(): String {
        return """
            package dev.igordesouza.orthos.runtime.generated

            import dev.igordesouza.orthos.runtime.signal.*

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

//package dev.igordesouza.orthos.plugin.generator
//
///**
// * Generates a stable list of all runtime signals.
// *
// * The generated object is referenced by OrthosRuntime
// * and MUST NOT be modified at runtime.
// */
//object OrthosSignalsGenerator {
//
//    fun generate(): String {
//        return """
//            package dev.igordesouza.orthos.runtime.signal.generated
//
//            import dev.igordesouza.orthos.runtime.signal.*
//
//            /**
//             * Generated at build-time by Orthos Gradle Plugin.
//             */
//            object OrthosSignals {
//
//                fun generated(): List<Signal> = listOf(
//                    BytecodeCanarySignal(),
//                    NativeAgreementSignal(),
//                    SignatureSignal(),
//                    RootSignal(),
//                    EmulatorSignal(),
//                    VirtualizationSignal(),
//                    CloneAppSignal()
//                )
//            }
//        """.trimIndent()
//    }
//}

//package dev.igordesouza.orthos.plugin.codegen
//
//import org.gradle.api.Project
//import java.io.File
//
///**
// * Generates the OrthosSignals object into the runtime module.
// */
//internal class OrthosSignalsGenerator(
//    private val project: Project
//) {
//
//    fun generate(
//        outputDir: File,
//        canaryValue: Long,
//        signatureHash: String
//    ) {
//        val pkg = "dev.igordesouza.orthos.runtime.generated"
//        val file = File(outputDir, "OrthosSignals.kt")
//
//        file.writeText(
//            """
//            package $pkg
//
//            import dev.igordesouza.orthos.runtime.signal.*
//
//            object OrthosSignals {
//                @JvmStatic
//                fun generated(): List<Signal> = listOf(
//                    RootSignal(),
//                    EmulatorSignal(),
//                    VirtualizationSignal(),
//                    CloneAppSignal(),
//                    SignatureSignal("$signatureHash"),
//                    NativeAgreementSignal(),
//                    BytecodeCanarySignal($canaryValue)
//                )
//            }
//            """.trimIndent()
//        )
//    }
//}
