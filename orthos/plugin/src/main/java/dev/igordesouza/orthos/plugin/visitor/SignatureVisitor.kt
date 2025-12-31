package dev.igordesouza.orthos.plugin.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * Injects the expected APK signature hash into bytecode.
 */
class SignatureVisitor(
    api: Int,
    private val className: String,
    private val expectedSignature: String,
    next: ClassVisitor
) : ClassVisitor(api, next) {

    override fun visitEnd() {
        injectSignatureField()
        super.visitEnd()
    }

    private fun injectSignatureField() {
        val field = cv.visitField(
            Opcodes.ACC_PRIVATE or
                    Opcodes.ACC_STATIC or
                    Opcodes.ACC_FINAL,
            "__orthos_expected_signature",
            "Ljava/lang/String;",
            null,
            expectedSignature
        )
        field.visitEnd()
    }
}



//package dev.igordesouza.orthos.plugin.visitor
//
//import dev.igordesouza.orthos.plugin.util.AsmUtils
//import org.objectweb.asm.ClassVisitor
//import org.objectweb.asm.Opcodes
//
///**
// * Injects the expected APK signature into bytecode.
// *
// * The signature is embedded as an obfuscated static field
// * and reconstructed at runtime.
// */
//class SignatureVisitor(
//    api: Int,
//    private val className: String,
//    private val signatureHash: String,
//    next: ClassVisitor
//) : ClassVisitor(api, next) {
//
//    private val mask = 0x5A5A5A5A
//
//    override fun visitEnd() {
//        injectSignatureField()
//        injectSignatureMethod()
//        super.visitEnd()
//    }
//
//    /**
//     * Injects an obfuscated signature hash.
//     */
//    private fun injectSignatureField() {
//        val obfuscated = signatureHash.hashCode() xor mask
//
//        cv.visitField(
//            Opcodes.ACC_PRIVATE or
//                    Opcodes.ACC_STATIC or
//                    Opcodes.ACC_FINAL,
//            "__orthos_signature",
//            "I",
//            null,
//            obfuscated
//        ).visitEnd()
//    }
//
//    /**
//     * Reconstructs the expected signature hash.
//     *
//     * private static int __orthos_signature_value()
//     */
//    private fun injectSignatureMethod() {
//        val mv = cv.visitMethod(
//            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC,
//            "__orthos_signature_value",
//            "()I",
//            null,
//            null
//        )
//
//        mv.visitCode()
//
//        mv.visitFieldInsn(
//            Opcodes.GETSTATIC,
//            className,
//            "__orthos_signature",
//            "I"
//        )
//
//        AsmUtils.pushInt(mv, mask)
//        mv.visitInsn(Opcodes.IXOR)
//
//        mv.visitInsn(Opcodes.IRETURN)
//        mv.visitMaxs(2, 0)
//        mv.visitEnd()
//    }
//}
