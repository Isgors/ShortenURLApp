package dev.igordesouza.orthos.runtime.signal

import dev.igordesouza.orthos.runtime.signal.impl.*

internal object RuntimeSignalRegistry {

    fun all(): List<Signal> = listOf(
        RootSignal(),
        EmulatorSignal(),
        BytecodeCanarySignal(),
        NativeAgreementSignal(),
        CloneAppSignal(),
        VirtualizationSignal()
    )
}
