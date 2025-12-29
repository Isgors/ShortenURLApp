package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.OrthosVerdict.Companion.safe

class PermissiveFailSafeHandler : FailSafeHandler {

    override fun onFailure(throwable: Throwable): OrthosVerdict {
        return OrthosVerdict.safe()
    }
}
