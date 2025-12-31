package dev.igordesouza.orthos.plugin.visitor

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import dev.igordesouza.orthos.plugin.visitor.*
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * AGP ASM factory used by the Android Gradle Plugin.
 */
abstract class OrthosAsmClassVisitorFactory :
    AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {

        val className = classContext.currentClassData.className

        var visitor = nextClassVisitor

        visitor = BytecodeCanaryVisitor(
            api = Opcodes.ASM9,
            className = className,
            next = visitor
        )

//        visitor = SignatureVisitor(
//            api = Opcodes.ASM9,
//            className = className,
//            expectedSignature = parameters.get().expectedSignatureSha256.get(),
//            next = visitor
//        )

        visitor = NativeAgreementVisitor(
            api = Opcodes.ASM9,
            className = className,
            next = visitor
        )

        return visitor
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }
}


///**
// * Parameters passed from Gradle to ASM visitors.
// */
//interface SignatureInstrumentationParams : InstrumentationParameters {
//
//    val expectedSignatureSha256: Property<String>
//}
