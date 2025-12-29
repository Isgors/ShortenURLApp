package dev.igordesouza.orthos.runtime.signal

import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.core.feature.FeatureSnapshot

data class SignalContext(
    val identity: ClientIdentity,
    val features: FeatureSnapshot
)
