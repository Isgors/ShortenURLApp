package dev.igordesouza.orthos.plugin.util

import org.objectweb.asm.*

/**
 * Generates minimal Android Application bytecode for ASM tests.
 */
object DummyApplicationClass {

    fun bytes(withOnCreate: Boolean = false): ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)

        cw.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC,
            "dev/orthos/sample/MainApplication",
            null,
            "android/app/Application",
            null
        )

        if (withOnCreate) {
            val mv = cw.visitMethod(
                Opcodes.ACC_PUBLIC,
                "onCreate",
                "()V",
                null,
                null
            )
            mv.visitCode()
            mv.visitInsn(Opcodes.RETURN)
            mv.visitMaxs(0, 1)
            mv.visitEnd()
        }

        cw.visitEnd()
        return cw.toByteArray()
    }
}
