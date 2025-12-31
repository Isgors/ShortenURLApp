package dev.igordesouza.orthos.plugin.unit

import org.objectweb.asm.ClassWriter
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Validates OrthosAsmVisitorFactory creation.
 */
class OrthosAsmVisitorFactoryTest {

    @Test
    fun `factory creates visitor successfully`() {
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val factory = OrthosAsmVisitorFactory()

        val visitor = factory.create(
            className = "dev/orthos/sample/MainApplication",
            next = writer
        )

        assertNotNull(visitor)
    }
}
