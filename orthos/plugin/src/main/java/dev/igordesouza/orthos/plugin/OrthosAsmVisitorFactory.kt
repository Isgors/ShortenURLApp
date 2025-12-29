package dev.igordesouza.orthos.plugin

import dev.igordesouza.orthos.plugin.visitor.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


/**
 * Factory responsável por encadear todos os ClassVisitors
 * utilizados pelo Orthos.
 *
 * A ordem é crítica:
 * 1. Canary
 * 2. Analyze
 * 3. Init hook
 */
class OrthosAsmVisitorFactory {

    fun create(
        className: String,
        next: ClassVisitor
    ): ClassVisitor {
        var visitor = next

        visitor = OrthosInitClassVisitor(
            api = Opcodes.ASM9,
            className = className,
            next = visitor
        )

        visitor = OrthosAnalyzeClassVisitor(
            api = Opcodes.ASM9,
            className = className,
            next = visitor
        )

        visitor = OrthosCanaryClassVisitor(
            api = Opcodes.ASM9,
            className = className,
            next = visitor
        )

        return visitor
    }
}
