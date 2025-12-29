package dev.igordesouza.orthos.plugin.util

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Utilitários ASM reutilizáveis.
 */
object AsmUtils {

    fun pushLong(mv: MethodVisitor, value: Long) {
        mv.visitLdcInsn(value)
    }

    fun xorLong(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.LXOR)
    }
}
