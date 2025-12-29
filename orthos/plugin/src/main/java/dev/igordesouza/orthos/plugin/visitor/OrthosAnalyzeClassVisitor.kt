package dev.igordesouza.orthos.plugin.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


/**
 * Injeta o método analyzeInjected(), que se torna
 * o ponto central de execução do Orthos em runtime.
 *
 * Este método NÃO contém lógica de segurança.
 * Ele apenas chama o runtime.
 */
class OrthosAnalyzeClassVisitor(
    api: Int,
    private val className: String,
    next: ClassVisitor
) : ClassVisitor(api, next) {

    override fun visitEnd() {
        injectAnalyzeMethod()
        super.visitEnd()
    }

    /**
     * private void analyzeInjected()
     */
    private fun injectAnalyzeMethod() {
        val mv = cv.visitMethod(
            Opcodes.ACC_PRIVATE,
            "analyzeInjected",
            "()V",
            null,
            null
        )

        mv.visitCode()

        // OrthosRuntime.analyze(this)
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "dev/orthos/runtime/OrthosRuntime",
            "analyze",
            "(Ljava/lang/Object;)V",
            false
        )

        mv.visitInsn(Opcodes.RETURN)
        mv.visitMaxs(2, 1)
        mv.visitEnd()
    }
}
