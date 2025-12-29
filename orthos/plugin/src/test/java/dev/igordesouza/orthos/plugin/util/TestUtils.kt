package dev.igordesouza.orthos.plugin.util

import dev.igordesouza.orthos.plugin.OrthosAsmVisitorFactory
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

/**
 * Applies Orthos ASM visitors to class bytecode.
 */
object TestUtils {

    fun transform(original: ByteArray): ByteArray {
        val reader = ClassReader(original)
        val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS)

        val visitor = OrthosAsmVisitorFactory()
            .create("dev/orthos/sample/MainApplication", writer)

        reader.accept(visitor, 0)
        return writer.toByteArray()
    }
}