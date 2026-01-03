package dev.igordesouza.orthos.plugin.visitor

import org.gradle.api.file.RegularFileProperty
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Injects a native agreement constant.
 *
 * This value is validated by NativeAgreementSignal
 * to ensure the native layer matches the expected build.
 */
class NativeAgreementVisitor(
    api: Int,
    private val className: String,
    private val keepRegistryFile: RegularFileProperty,
    next: ClassVisitor
) : ClassVisitor(api, next) {

    private val agreementValue: Long = 0xCAFEBABEL

    override fun visitEnd() {
        registerKeep()
        injectNativeAgreement()
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
     * private static int __orthos_native_agreement()
     */
    private fun injectNativeAgreement() {
        val mv = cv.visitMethod(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC or Opcodes.ACC_SYNTHETIC,
            "__orthos_native_agreement",
            "()J",
            null,
            null
        )

        mv.visitCode()
        mv.visitLdcInsn(agreementValue)
        mv.visitInsn(Opcodes.LRETURN)
        mv.visitMaxs(2, 0)
        mv.visitEnd()
    }
}