package dev.igordesouza.orthos.plugin

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationContext
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor

/**
 * Transformer ASM oficial do Orthos.
 *
 * Ele apenas conecta o AGP com a VisitorFactory.
 */
class OrthosClassTransformer(
    override val parameters: Property<InstrumentationParameters.None>,
    override val instrumentationContext: InstrumentationContext
) : AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return OrthosAsmVisitorFactory()
            .create(
                className = classContext.currentClassData.className.replace('.', '/'),
                next = nextClassVisitor
            )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        // Evita instrumentar libs do sistema
        return !classData.className.startsWith("android.")
    }
}
