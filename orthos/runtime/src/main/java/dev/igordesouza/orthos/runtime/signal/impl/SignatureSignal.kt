package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.core.signal.*
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.runtime.util.SignatureUtils

/**
 * Verifies APK signature integrity.
 */
class SignatureSignal : Signal {

    override val id = SignalId.SIGNATURE
    override val type = SignalType.IDENTITY

    override fun collect(context: RuntimeSignalContext): SignalResult {
        return try {
            val method = context.applicationContext
                .javaClass
                .getDeclaredMethod("__orthos_signature")

            method.isAccessible = true
            val expected = method.invoke(null) as String

            val actual = SignatureUtils.currentSignature(context.applicationContext)

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
