package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.signal.SignalType
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext

/**
 * Detects native layer tampering by validating the
 * agreement between Java and native layers.
 */
class NativeAgreementSignal : Signal {

    override val id = SignalId.NATIVE_AGREEMENT
    override val type = SignalType.TAMPERING

    override fun collect(context: RuntimeSignalContext): SignalResult {
        return try {
            val expectedMethod = context.applicationContext
                .javaClass
                .getDeclaredMethod("__orthos_native_agreement")

            expectedMethod.isAccessible = true
            val expected = expectedMethod.invoke(null) as Long

            val actual = NativeAgreement.nativeValue()

            SignalResult(
                signalId = id,
                signalType = type,
                triggered = expected != actual,
                confidence = 1.0f
            )
        } catch (t: Throwable) {
            SignalResult(
                signalId = id,
                signalType = type,
                triggered = true,
                confidence = 1.0f
            )
        }
    }
}

