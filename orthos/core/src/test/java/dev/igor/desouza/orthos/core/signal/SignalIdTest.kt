package dev.igordesouza.orthos.core.signal

import dev.igordesouza.orthos.core.signal.SignalId
import kotlin.test.Test
import kotlin.test.assertEquals

class SignalIdTest {

    @Test
    fun `signal ids are unique`() {
        val ids = SignalId.entries.map { it.name }
        assertEquals(ids.size, ids.toSet().size)
    }
}
