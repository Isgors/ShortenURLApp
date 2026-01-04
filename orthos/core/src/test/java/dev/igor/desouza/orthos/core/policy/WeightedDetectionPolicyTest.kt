package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import kotlin.test.Test
import kotlin.test.assertEquals

class WeightedDetectionPolicyTest {

    @Test
    fun `score above threshold results in tampered`() {
        val policy = WeightedDetectionPolicy(threshold = 50)

        val verdict = policy.evaluate(
            listOf(
                SignalResult(
                    signalId = SignalId.EMULATOR,
                    triggered = true,
                    confidence = 0.8f
                )
            )
        )

        assertEquals(RuntimeState.TAMPERED, verdict.state)
    }

    @Test
    fun `score below threshold results in safe`() {
        val policy = WeightedDetectionPolicy(threshold = 90)

        val verdict = policy.evaluate(
            listOf(
                SignalResult(
                    signalId = SignalId.EMULATOR,
                    triggered = true,
                    confidence = 0.5f
                )
            )
        )

        assertEquals(RuntimeState.SAFE, verdict.state)
    }
}
