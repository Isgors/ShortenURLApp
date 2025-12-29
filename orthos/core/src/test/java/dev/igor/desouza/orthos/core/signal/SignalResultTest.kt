package dev.igordesouza.orthos.core.signal

import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SignalResultTest {

    @Test
    fun `signal result stores all values correctly`() {
        val result = SignalResult(
            signalId = SignalId.EMULATOR,
            triggered = true,
            confidence = 0.85f,
            metadata = mapOf("model" to "sdk_gphone")
        )

        assertEquals(SignalId.EMULATOR, result.signalId)
        assertTrue(result.triggered)
        assertEquals(0.85f, result.confidence)
        assertEquals("sdk_gphone", result.metadata["model"])
    }

    @Test
    fun `metadata defaults to empty map`() {
        val result = SignalResult(
            signalId = SignalId.SIGNATURE,
            triggered = false,
            confidence = 0.0f
        )

        assertFalse(result.triggered)
        assertTrue(result.metadata.isEmpty())
    }
}
