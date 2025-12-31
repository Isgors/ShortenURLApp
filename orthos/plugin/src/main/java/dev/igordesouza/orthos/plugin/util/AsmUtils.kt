package dev.igordesouza.orthos.plugin.util

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Common reusable ASM utilities.
 *
 * Centralizes low-level bytecode instructions to
 * avoid duplication and mistakes.
 */
object AsmUtils {

    /**
     * Pushes a long constant onto the operand stack.
     */
    fun pushLong(mv: MethodVisitor, value: Long) {
        mv.visitLdcInsn(value)
    }

    /**
     * Pushes an int constant onto the operand stack.
     */
    fun pushInt(mv: MethodVisitor, value: Int) {
        when (value) {
            -1 -> mv.visitInsn(Opcodes.ICONST_M1)
            0 -> mv.visitInsn(Opcodes.ICONST_0)
            1 -> mv.visitInsn(Opcodes.ICONST_1)
            2 -> mv.visitInsn(Opcodes.ICONST_2)
            3 -> mv.visitInsn(Opcodes.ICONST_3)
            4 -> mv.visitInsn(Opcodes.ICONST_4)
            5 -> mv.visitInsn(Opcodes.ICONST_5)
            else -> mv.visitLdcInsn(value)
        }
    }

    /**
     * Performs XOR on two longs.
     */
    fun xorLong(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.LXOR)
    }

    /**
     * Performs XOR on two ints.
     */
    fun xorInt(mv: MethodVisitor) {
        mv.visitInsn(Opcodes.IXOR)
    }
}
