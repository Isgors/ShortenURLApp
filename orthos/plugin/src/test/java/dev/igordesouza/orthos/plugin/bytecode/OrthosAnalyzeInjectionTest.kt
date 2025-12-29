package dev.igordesouza.orthos.plugin.bytecode

import dev.igordesouza.orthos.plugin.util.BytecodeInspector
import dev.igordesouza.orthos.plugin.util.DummyApplicationClass
import dev.igordesouza.orthos.plugin.util.TestUtils
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Ensures OrthosAnalyzeClassVisitor injects analyze invocation.
 */
class OrthosAnalyzeInjectionTest {

    @Test
    fun `analyze method call is injected`() {
        val original = DummyApplicationClass.bytes()
        val transformed = TestUtils.transform(original)

        assertTrue(
            BytecodeInspector.containsMethodInvocation(
                transformed,
                owner = "dev/orthos/runtime/OrthosRuntime",
                methodName = "analyze"
            )
        )
    }
}
