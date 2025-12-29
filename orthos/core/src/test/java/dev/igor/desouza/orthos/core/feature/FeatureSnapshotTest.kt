package dev.igordesouza.orthos.core.feature

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.feature.SignalFeatureConfig
import dev.igordesouza.orthos.core.feature.SignalGroupConfig
import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.core.policy.StrictDetectionPolicy
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalType
import kotlin.test.Test
import kotlin.test.assertEquals

class FeatureSnapshotTest {

    @Test
    fun `feature snapshot holds resolved configuration`() {
        val identity = ClientIdentity("client-123", "install-123")

        val snapshot = FeatureSnapshot(
            identity = identity,
            signals = mapOf(
                SignalId.EMULATOR to SignalFeatureConfig(
                    signalId = SignalId.EMULATOR,
                    enabled = true,
                    weight = 50
                )
            ),
            groups = mapOf(
                SignalType.ENVIRONMENT to SignalGroupConfig(
                    type = SignalType.ENVIRONMENT,
                    enabled = true,
                    defaultWeight = 40
                )
            ),
            policy = StrictDetectionPolicy(threshold = 1)
        )

        assertEquals(identity, snapshot.identity)
        assertEquals(1, snapshot.signals.size)
        assertEquals(1, snapshot.groups.size)
    }
}