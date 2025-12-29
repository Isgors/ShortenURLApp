package dev.igordesouza.orthos.runtime.identity

import android.content.Context
import dev.igordesouza.orthos.core.identity.ClientIdentity

/**
 * Resolves the identity used during signal execution
 * and policy evaluation.
 *
 * Implementations must return a stable identity
 * during a single app lifecycle.
 *
 * This identity is used to:
 * - Resolve feature flags
 * - Select detection policies
 * - Apply gradual rollouts
 */
interface RuntimeIdentityProvider {

    fun provide(): ClientIdentity
}
