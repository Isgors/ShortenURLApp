package dev.igordesouza.orthos.plugin.visitor

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
    next: ClassVisitor
) : ClassVisitor(api, next) {

    private val agreementValue: Long = 0xCAFEBABE

    override fun visitEnd() {
        injectNativeAgreement()
        super.visitEnd()
    }

    /**
     * private static int __orthos_native_agreement()
     */
    private fun injectNativeAgreement() {
        val mv = cv.visitMethod(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC,
            "__orthos_native_agreement",
            "()I",
            null,
            null
        )

        mv.visitCode()
        mv.visitLdcInsn(agreementValue)
        mv.visitInsn(Opcodes.IRETURN)
        mv.visitMaxs(1, 0)
        mv.visitEnd()
    }
}
