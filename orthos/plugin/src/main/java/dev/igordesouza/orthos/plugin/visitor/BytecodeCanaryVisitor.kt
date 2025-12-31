package dev.igordesouza.orthos.plugin.visitor

import dev.igordesouza.orthos.plugin.canary.CanarySeedGenerator
import dev.igordesouza.orthos.plugin.util.AsmUtils
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Injects a hardened bytecode canary into the target class.
 *
 * The canary is stored in an obfuscated static field and
 * reconstructed at runtime via XOR.
 *
 * Any bytecode modification breaks the reconstructed value.
 */
class BytecodeCanaryVisitor(
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
     * Injects the static obfuscated canary field.
     *
     * private static final long __orthos_canary;
     */
    private fun injectCanaryField() {
        val obfuscated = seed xor mask

        cv.visitField(
            Opcodes.ACC_PRIVATE or
                    Opcodes.ACC_STATIC or
                    Opcodes.ACC_FINAL,
            "__orthos_canary",
            "J",
            null,
            obfuscated
        ).visitEnd()
    }

    /**
     * Injects a method that reconstructs the canary at runtime.
     *
     * private static long __orthos_canary_value()
     */
    private fun injectCanaryMethod() {
        val mv = cv.visitMethod(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC,
            "__orthos_canary_value",
            "()J",
            null,
            null
        )

        mv.visitCode()

        // Load obfuscated value
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
