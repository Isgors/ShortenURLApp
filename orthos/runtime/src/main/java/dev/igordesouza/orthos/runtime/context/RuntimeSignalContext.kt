package dev.igordesouza.orthos.runtime.context

import android.content.Context
import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.runtime.time.Clock

/**
 * Provides all runtime dependencies required by signals.
 *
 * This object is immutable during a single execution cycle.
 */
class RuntimeSignalContext(
    val applicationContext: Context,
    val identity: ClientIdentity,
    val clock: Clock
)
