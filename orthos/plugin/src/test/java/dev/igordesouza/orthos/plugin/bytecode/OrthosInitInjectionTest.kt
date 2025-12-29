package dev.igordesouza.orthos.plugin.bytecode

import dev.igordesouza.orthos.plugin.util.BytecodeInspector
import dev.igordesouza.orthos.plugin.util.DummyApplicationClass
import dev.igordesouza.orthos.plugin.util.TestUtils
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Ensures Application.onCreate is instrumented.
 */
class OrthosInitInjectionTest {

    @Test
    fun `onCreate method is instrumented`() {
        val original = DummyApplicationClass.bytes(withOnCreate = true)
        val transformed = TestUtils.transform(original)

        assertTrue(
            BytecodeInspector.containsMethod(
                transformed,
                "onCreate"
            )
        )
    }

    @Test
    fun `onCreate contains OrthosRuntime analyze invocation`() {
        // Given: dummy Application bytecode
        val originalBytes = DummyApplicationClass.bytes(withOnCreate = true)

        // When: transformed by Orthos plugin
        val transformedBytes = TestUtils.transform(originalBytes)

        // Then: bytecode contains injected call
        val instructions = BytecodeInspector.disassemble(transformedBytes)

        assertTrue(
            instructions.any { it.contains("dev/orthos/runtime/OrthosRuntime.analyze") },
            "Expected OrthosRuntime.analyze to be injected into onCreate()"
        )
    }
}
