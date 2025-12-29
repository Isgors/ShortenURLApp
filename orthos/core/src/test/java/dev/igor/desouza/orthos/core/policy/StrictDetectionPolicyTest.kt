package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.core.policy.StrictDetectionPolicy
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import kotlin.test.Test
import kotlin.test.assertEquals

class StrictDetectionPolicyTest {

    @Test
    fun `any triggered signal results in tampered`() {
        val policy = StrictDetectionPolicy(threshold = 100)

        val verdict = policy.evaluate(
            listOf(
                SignalResult(
                    signalId = SignalId.SIGNATURE,
                    triggered = true,
                    confidence = 1.0f
                )
            )
        )
        assertEquals(RuntimeState.TAMPERED, verdict.state)
    }

    @Test
    fun `no triggered signals results in safe verdict`() {
        val policy = StrictDetectionPolicy(threshold = 100)

        val verdict = policy.evaluate(
            listOf(
                SignalResult(
                    signalId = SignalId.SIGNATURE,
                    triggered = false,
                    confidence = 1.0f
                )
            )
        )
        assertEquals(RuntimeState.SAFE, verdict.state)
    }
}


