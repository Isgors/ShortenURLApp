package dev.igordesouza.orthos.plugin.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Garante que analyzeInjected() seja chamado
 * automaticamente durante o ciclo de vida.
 */
class OrthosInitClassVisitor(
    api: Int,
    private val className: String,
    next: ClassVisitor
) : ClassVisitor(api, next) {

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (name == "onCreate") { //) {
            return object : MethodVisitor(api, mv) {

                override fun visitCode() {
                    super.visitCode()

                    mv.visitVarInsn(Opcodes.ALOAD, 0)
                    mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        className,
                        "analyzeInjected",
                        "()V",
                        false
                    )
                }
            }
        }

        return mv
    }
}