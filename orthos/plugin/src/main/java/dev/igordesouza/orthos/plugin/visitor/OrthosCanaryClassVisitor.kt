package dev.igordesouza.orthos.plugin.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import dev.igordesouza.orthos.plugin.canary.CanarySeedGenerator
import dev.igordesouza.orthos.plugin.util.AsmUtils

/**
 * Injeta:
 * 1. Um campo estático canary ofuscado
 * 2. Um método que reconstrói o canary em runtime via XOR
 *
 * Qualquer modificação no bytecode quebra o valor final.
 */
class OrthosCanaryClassVisitor(
    api: Int,
    private val className: String,
    next: ClassVisitor
) : ClassVisitor(api, next) {

    private val seed: Long = CanarySeedGenerator.generateSeed()
    private val mask: Long = -7046029254386353131L

    override fun visitEnd() {
        injectCanaryField()
        injectCanaryMethod()
        super.visitEnd()
    }

    /**
     * Injeta o campo estático com valor XOR.
     */
    private fun injectCanaryField() {
        val obfuscated = seed xor mask

        val field = cv.visitField(
            Opcodes.ACC_PRIVATE or
                    Opcodes.ACC_STATIC or
                    Opcodes.ACC_FINAL,
            "__orthos_canary",
            "J",
            null,
            obfuscated
        )
        field.visitEnd()
    }

    /**
     * Injeta método que reconstitui o canary original em runtime.
     *
     * private static long __orthos_canary_value()
     */
    private fun injectCanaryMethod() {
        val mv = cv.visitMethod(
            Opcodes.ACC_PRIVATE or
                    Opcodes.ACC_STATIC,
            "__orthos_canary_value",
            "()J",
            null,
            null
        )

        mv.visitCode()

        // Load obfuscated canary
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            className,
            "__orthos_canary",
            "J"
        )

        // Load mask
        AsmUtils.pushLong(mv, mask)

        // XOR
        AsmUtils.xorLong(mv)

        mv.visitInsn(Opcodes.LRETURN)
        mv.visitMaxs(4, 0)
        mv.visitEnd()
    }
}