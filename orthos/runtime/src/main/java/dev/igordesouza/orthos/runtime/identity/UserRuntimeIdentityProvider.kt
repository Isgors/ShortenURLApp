package dev.igordesouza.orthos.runtime.identity

import android.content.Context
import dev.igordesouza.orthos.core.identity.ClientIdentity

/**
 * Identity provider that supports user-scoped feature flags.
 */
class UserRuntimeIdentityProvider(
    private val context: Context,
    private val installIdProvider: InstallIdProvider,
    private val userIdProvider: () -> String?
) : RuntimeIdentityProvider {

    override fun provide(): ClientIdentity {
        return ClientIdentity(
            appId = context.packageName,
            installId = installIdProvider.get(),
            userId = userIdProvider()
        )
    }
}
