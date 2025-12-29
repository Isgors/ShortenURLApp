package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.policy.GradedDetectionPolicy
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.verdict.RuntimeState
import kotlin.test.Test
import kotlin.test.assertEquals

class GradedDetectionPolicyTest {

    @Test
    fun `returns suspicious state`() {

        val policy = GradedDetectionPolicy(
            suspiciousThreshold = 50,
            tamperedThreshold = 100
        )
        val verdict = policy.evaluate(
            listOf(
                SignalResult(
                    signalId = SignalId.EMULATOR,
                    triggered = true,
                    confidence = 0.6f
                )
            )
        )

        assertEquals(RuntimeState.SUSPICIOUS, verdict.state)
    }

    @Test
    fun `returns tampered state`() {

        val policy = GradedDetectionPolicy(
            suspiciousThreshold = 50,
            tamperedThreshold = 100
        )

        val verdict = policy.evaluate(
            listOf(
                SignalResult(
                    signalId = SignalId.EMULATOR,
                    triggered = true,
                    confidence = 1.0f
                )
            )
        )

        assertEquals(RuntimeState.TAMPERED, verdict.state)
    }
}
