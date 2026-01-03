package dev.igordesouza.orthos.plugin.visitor

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * ASM factory responsible for wiring all Orthos bytecode visitors.
 *
 * This factory does NOT perform any injection itself.
 * It only composes visitors in the correct order.
 *
 * Order (outer → inner):
 * 1. SignatureVisitor
 * 2. NativeAgreementVisitor
 * 3. BytecodeCanaryVisitor
 */
abstract class OrthosAsmClassVisitorFactory :
    AsmClassVisitorFactory<OrthosInstrumentationParams> {

    override fun isInstrumentable(classData: ClassData): Boolean =
        classData.className.startsWith("dev/igordesouza/")

    override fun createClassVisitor(
        classContext: ClassContext,
        next: ClassVisitor
    ): ClassVisitor {

        val className = classContext.currentClassData.className
        val keepRegistryFile = parameters.get().keepRegistryFile

        var visitor: ClassVisitor = next

        // 3️⃣ Bytecode Canary (must be last)
        visitor = BytecodeCanaryVisitor(
            api = Opcodes.ASM9,
            className = className,
            keepRegistryFile = keepRegistryFile,
            next = visitor
        )

        // 2️⃣ Native Agreement
        visitor = NativeAgreementVisitor(
            api = Opcodes.ASM9,
            className = className,
            keepRegistryFile = keepRegistryFile,
            next = visitor
        )

        return visitor
    }
}


//package dev.igordesouza.orthos.plugin.visitor
//
//
//import com.android.build.api.instrumentation.AsmClassVisitorFactory
//import com.android.build.api.instrumentation.ClassContext
//import com.android.build.api.instrumentation.ClassData
//import org.objectweb.asm.ClassVisitor
//import org.objectweb.asm.Opcodes
//import java.util.concurrent.atomic.AtomicBoolean
//
///**
// * ASM factory responsible for wiring Orthos bytecode visitors.
// *
// * Also persists keep-registry entries in a file-based registry.
// */
//abstract class OrthosAsmClassVisitorFactory :
//    AsmClassVisitorFactory<OrthosInstrumentationParams> {
//
//    override fun isInstrumentable(classData: ClassData): Boolean = true
//
//    override fun createClassVisitor(
//        classContext: ClassContext,
//        nextClassVisitor: ClassVisitor
//    ): ClassVisitor {
//
//        val className = classContext.currentClassData.className
//
//        return object : ClassVisitor(Opcodes.ASM9, nextClassVisitor) {
//
//            private val registered = AtomicBoolean(false)
//
//            override fun visitEnd() {
//                registerKeepClassOnce(className)
//                super.visitEnd()
//            }
//
//            /**
//             * Registers the class name in the keep-registry file.
//             *
//             * This is file-based to survive:
//             * - classloader isolation
//             * - worker processes
//             * - parallel execution
//             */
//            private fun registerKeepClassOnce(className: String) {
//                if (registered.compareAndSet(false, true)) {
//                    val file = parameters.get().keepRegistryFile.get().asFile
//                    file.parentFile.mkdirs()
//                    file.appendText("$className\n")
//                }
//            }
//        }
//    }
//}