package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import java.io.File
import java.security.MessageDigest

/**
 * Detects native layer tampering by validating the
 * presence and integrity of native libraries.
 */
class NativeAgreementSignal(
    private val libraryName: String,
    private val expectedHash: String
) : Signal {

    override val signalId: SignalId = SignalId.NATIVE_AGREEMENT

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val nativeFile = findNativeLibrary(context)

        if (nativeFile == null || !nativeFile.exists()) {
            return SignalResult(
                signalId = signalId,
                triggered = true,
                confidence = 1.0f,
                metadata = mapOf("reason" to "native_library_missing")
            )
        }

        val currentHash = sha256(nativeFile)
        val triggered = currentHash != expectedHash

        return SignalResult(
            signalId = signalId,
            triggered = triggered,
            confidence = if (triggered) 1.0f else 0.0f,
            metadata = mapOf(
                "expected" to expectedHash,
                "current" to currentHash
            )
        )
    }

    private fun findNativeLibrary(
        context: RuntimeSignalContext
    ): File? {
        val appInfo = context.applicationContext.applicationInfo
        val libDir = File(appInfo.nativeLibraryDir)

        return libDir
            .listFiles()
            ?.firstOrNull { it.name.contains(libraryName) }
    }

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8_192)
            var read: Int
            while (input.read(buffer).also { read = it } > 0) {
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest()
            .joinToString("") { "%02x".format(it) }
    }
}
