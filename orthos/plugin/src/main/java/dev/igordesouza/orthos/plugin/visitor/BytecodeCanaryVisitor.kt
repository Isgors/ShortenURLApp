package dev.igordesouza.orthos.plugin.visitor

import dev.igordesouza.orthos.plugin.canary.CanarySeedGenerator
import dev.igordesouza.orthos.plugin.util.AsmUtils
import org.gradle.api.file.RegularFileProperty
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Injects a hardened bytecode canary.
 *
 * The canary is stored obfuscated and reconstructed at runtime.
 * Any bytecode modification invalidates the value.
 */
class BytecodeCanaryVisitor(
    api: Int,
    private val className: String,
    private val keepRegistryFile: RegularFileProperty,
    next: ClassVisitor
) : ClassVisitor(api, next) {

    private val seed: Long = CanarySeedGenerator.generateSeed()
    private val mask: Long = -7046029254386353131L

    override fun visitEnd() {
        registerKeep()
        injectCanaryField()
        injectCanaryMethod()
        super.visitEnd()
    }

    /**
     * Registers this class in the keep-registry.
     */
    private fun registerKeep() {
        val file = keepRegistryFile.get().asFile
        file.parentFile.mkdirs()
        file.appendText("$className\n")
    }

    /**
     * private static final long __orthos_canary;
     */
    private fun injectCanaryField() {
        val obfuscated = seed xor mask

        cv.visitField(
            Opcodes.ACC_PRIVATE or
                    Opcodes.ACC_STATIC or
                    Opcodes.ACC_FINAL or
                    Opcodes.ACC_SYNTHETIC,
            "__orthos_canary",
            "J",
            null,
            obfuscated
        ).visitEnd()
    }

    /**
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

        // GETSTATIC __orthos_canary
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            className,
            "__orthos_canary",
            "J"
        )

        // push mask
        AsmUtils.pushLong(mv, mask)

        // XOR
        AsmUtils.xorLong(mv)

        mv.visitInsn(Opcodes.LRETURN)
        mv.visitMaxs(4, 0)
        mv.visitEnd()
    }
}
