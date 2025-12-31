package dev.igordesouza.orthos.runtime.signal.impl

import android.content.pm.PackageManager
import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.signal.SignalType
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import java.io.File

/**
 * Detects application execution inside virtualization or app-cloning environments.
 *
 * This signal checks:
 * - Known virtualization package names
 * - Suspicious filesystem paths
 *
 * This is a heuristic signal and should be combined with others.
 */
class VirtualizationSignal : Signal {

    override val id: SignalId = SignalId.VIRTUALIZATION

    override val type = SignalType.RUNTIME

    private val knownVirtualizationPackages = listOf(
        "com.lbe.parallel.intl",
        "com.parallel.space",
        "com.dualspace.multispace",
        "com.applisto.appcloner",
        "com.lbe.parallel"
    )

    private val suspiciousPaths = listOf(
        "/data/user/999",
        "/data/data/com.lbe.parallel",
        "/data/data/com.parallel.space"
    )

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val packageTriggered = hasVirtualizationPackages(context)
        val pathTriggered = hasSuspiciousPaths()

        val triggered = packageTriggered || pathTriggered

        return SignalResult(
            signalId = id,
            signalType = type,
            triggered = triggered,
            confidence = when {
                packageTriggered && pathTriggered -> 0.95f
                packageTriggered || pathTriggered -> 0.7f
                else -> 0.0f
            },
            metadata = buildMetadata(packageTriggered, pathTriggered)
        )
    }

    private fun hasVirtualizationPackages(context: RuntimeSignalContext): Boolean {
        val pm: PackageManager = context.applicationContext.packageManager
        return knownVirtualizationPackages.any { pkg ->
            try {
                pm.getPackageInfo(pkg, 0)
                true
            } catch (_: PackageManager.NameNotFoundException) {
                false
            }
        }
    }

    private fun hasSuspiciousPaths(): Boolean {
        return suspiciousPaths.any { path ->
            File(path).exists()
        }
    }

    private fun buildMetadata(
        packageTriggered: Boolean,
        pathTriggered: Boolean
    ): Map<String, String> {
        if (!packageTriggered && !pathTriggered) return emptyMap()

        return mapOf(
            "package_check" to packageTriggered.toString(),
            "path_check" to pathTriggered.toString()
        )
    }
}
