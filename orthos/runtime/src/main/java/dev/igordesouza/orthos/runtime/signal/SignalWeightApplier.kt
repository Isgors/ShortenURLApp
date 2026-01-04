package dev.igordesouza.orthos.runtime.signal

import dev.igordesouza.orthos.runtime.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.logging.OrthosLogger

/**
 * Applies signal and group weights to raw signal results.
 *
 * This is the ONLY place where FeatureSnapshot weights are used.
 */
class SignalWeightApplier {

    fun apply(
        rawResults: List<SignalResult>,
        snapshot: FeatureSnapshot
    ): List<SignalResult> {

        return rawResults.map { result ->

            val signalConfig = snapshot.signals[result.signalId]
            val groupConfig = snapshot.groups[result.signalType]

            val weight = when {
                signalConfig?.enabled == true ->
                    signalConfig.weight

                groupConfig?.enabled == true ->
                    groupConfig.defaultWeight

                else -> 0
            }

            val weightedConfidence =
                if (result.triggered)
                    result.confidence * weight
                else
                    0.0

            OrthosLogger.debug(
                "Weight applied: %s base=%f weight=%d final=%f",
                result.signalId,
                result.confidence,
                weight,
                weightedConfidence
            )

            result.copy(confidence = weightedConfidence)
        }
    }
}
