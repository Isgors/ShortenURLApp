package dev.igordesouza.orthos.runtime.identity

import android.content.Context
import dev.igordesouza.orthos.core.identity.ClientIdentity

/**
 * Default identity provider.
 *
 * Identity is scoped to:
 * - appId
 * - installId
 */
class DefaultRuntimeIdentityProvider(
    private val context: Context,
    private val installIdProvider: InstallIdProvider
) : RuntimeIdentityProvider {

    override fun provide(): ClientIdentity {
        return ClientIdentity(
            appId = context.packageName,
            installId = installIdProvider.get(),
            userId = null
        )
    }
}

