package dev.igordesouza.orthos.core.signal

import dev.igordesouza.orthos.core.signal.SignalType
import kotlin.test.Test
import kotlin.test.assertNotNull

class SignalTypeTest {

    @Test
    fun `all signal types are defined`() {
        SignalType.entries.forEach {
            assertNotNull(it)
        }
    }
}
