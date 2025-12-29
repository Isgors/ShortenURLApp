package dev.igordesouza.orthos.runtime.signal.impl

import android.content.pm.PackageManager
import android.os.Build
import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import java.security.MessageDigest

/**
 * Detects APK signature tampering.
 *
 * Compares the runtime APK signature hash against the
 * expected signature injected at build time.
 */
class SignatureSignal(
    private val expectedSignatureHash: String
) : Signal {

    override val signalId: SignalId = SignalId.SIGNATURE

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val currentHash = getCurrentSignatureHash(context)

        val triggered = currentHash != null &&
            currentHash != expectedSignatureHash

        return SignalResult(
            signalId = signalId,
            triggered = triggered,
            confidence = if (triggered) 1.0f else 0.0f,
            metadata = mapOf(
                "expected" to expectedSignatureHash,
                "current" to (currentHash ?: "unknown")
            )
        )
    }

    private fun getCurrentSignatureHash(
        context: RuntimeSignalContext
    ): String? {
        return try {
            val pm = context.applicationContext.packageManager
            val pkgName = context.applicationContext.packageName

            val signatures =
                pm.getPackageInfo(
                    pkgName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                ).signingInfo?.apkContentsSigners

            val cert = signatures?.firstOrNull()?.toByteArray() ?: return null
            sha256(cert)
        } catch (_: Exception) {
            null
        }
    }

    private fun sha256(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(bytes)
            .joinToString("") { "%02x".format(it) }
    }
}
