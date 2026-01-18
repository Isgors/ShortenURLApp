package dev.igordesouza.orthos.sdk

import dev.igordesouza.orthos.runtime.policy.failsafe.ConservativeFailSafeHandler
import dev.igordesouza.orthos.runtime.policy.failsafe.FailSafeHandler

/**
 * SDK-level configuration surface.
 */
data class OrthosConfig(
    val failSafeHandler: FailSafeHandler = ConservativeFailSafeHandler()
)
