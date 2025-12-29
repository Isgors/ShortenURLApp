package dev.igordesouza.orthos.plugin.bytecode

import dev.igordesouza.orthos.plugin.util.BytecodeInspector
import dev.igordesouza.orthos.plugin.util.DummyApplicationClass
import dev.igordesouza.orthos.plugin.util.TestUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class OrthosCanaryInjectionTest {

    @Test
    fun `canary field is injected`() {
        val original = DummyApplicationClass.bytes()
        val transformed = TestUtils.transform(original)

        assertTrue(
            BytecodeInspector.containsField(
                transformed,
                "__orthos_canary"
            )
        )
    }
}
