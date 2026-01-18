package dev.igordesouza.orthos.sdk

import android.content.Context
import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.runtime.OrthosRuntime
import dev.igordesouza.orthos.runtime.policy.failsafe.FailSafeHandler

internal class RealOrthosRuntime(
    context: Context,
    failSafeHandler: FailSafeHandler
) : OrthosRuntimeApi {

    private val runtime: OrthosRuntime =
        OrthosRuntime.Builder(context)
            .withFailSafeHandler(failSafeHandler)
            .build()

    override fun evaluate(): OrthosVerdict = runtime.evaluate()
}
