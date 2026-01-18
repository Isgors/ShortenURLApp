package dev.igordesouza.orthos.runtime

import android.content.Context
import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import dev.igordesouza.orthos.runtime.executor.DefaultSignalExecutor
import dev.igordesouza.orthos.runtime.executor.SignalExecutor
import dev.igordesouza.orthos.runtime.feature.DefaultFeatureSnapshotProvider
import dev.igordesouza.orthos.runtime.feature.FeatureSnapshotProvider
import dev.igordesouza.orthos.runtime.feature.repository.FeatureRepository
import dev.igordesouza.orthos.runtime.feature.repository.FeatureRepositoryFactory
import dev.igordesouza.orthos.runtime.identity.DefaultRuntimeIdentityProvider
import dev.igordesouza.orthos.runtime.identity.InstallIdProvider
import dev.igordesouza.orthos.runtime.identity.RuntimeIdentityProvider
import dev.igordesouza.orthos.runtime.logging.OrthosLogger
import dev.igordesouza.orthos.runtime.policy.PolicyDslFactory
import dev.igordesouza.orthos.runtime.policy.PolicyEvaluator
import dev.igordesouza.orthos.runtime.policy.failsafe.ConservativeFailSafeHandler
import dev.igordesouza.orthos.runtime.policy.failsafe.FailSafeHandler
import dev.igordesouza.orthos.runtime.signal.RuntimeSignalRegistry
import dev.igordesouza.orthos.runtime.signal.SignalWeightApplier
import dev.igordesouza.orthos.runtime.time.Clock
import dev.igordesouza.orthos.runtime.time.SystemClock

/**
 * Main runtime entry point for Orthos evaluation.
 */
class OrthosRuntime internal constructor(
    private val applicationContext: Context,
    private val identityProvider: RuntimeIdentityProvider,
    private val snapshotProvider: FeatureSnapshotProvider,
    private val signalExecutor: SignalExecutor,
    private val policyEvaluator: PolicyEvaluator,
    private val weightApplier: SignalWeightApplier,
    private val failSafeHandler: FailSafeHandler,
    private val clock: Clock
) {

    fun evaluate(): OrthosVerdict {
        OrthosLogger.info("🚀 Orthos evaluation started")

        // ──────────────────────────────
        // Identity
        // ──────────────────────────────
        val identity = identityProvider.provide()
        OrthosLogger.debug(
            "Identity resolved: appId=%s installId=%s userId=%s",
            identity.appId,
            identity.installId,
            identity.userId
        )

        // ──────────────────────────────
        // Snapshot
        // ──────────────────────────────
        val snapshot = snapshotProvider.get(identity)
        OrthosLogger.debug(
            "Snapshot loaded: signals=%d groups=%d policy=%s",
            snapshot.signals.size,
            snapshot.groups.size,
            snapshot.policy.type
        )

        // ──────────────────────────────
        // Context
        // ──────────────────────────────
        val context = RuntimeSignalContext(
            applicationContext = applicationContext,
            identity = identity,
            clock = clock
        )

        // ──────────────────────────────
        // Policy resolution (DSL + fail-safe)
        // ──────────────────────────────
        val policyDefinition = try {
            OrthosLogger.debug("Resolving policy via DSL factory")
            PolicyDslFactory(failSafeHandler)
                .buildPolicyDefinition(snapshot.policy)
                .also {
                    OrthosLogger.debug(
                        "Policy resolved successfully: rules=%d",
                        it.rules.size
                    )
                }
        } catch (t: Throwable) {
            OrthosLogger.error(
                message = "Policy resolution failed, applying fail-safe: ${t.message}"
            )
            failSafeHandler.fallback(t)
        }

        policyDefinition.rules.forEach {
            OrthosLogger.debug(
                "Policy rule: minScore=%d state=%s",
                it.minScore,
                it.state
            )
        }

        // ──────────────────────────────
        // Signal selection
        // ──────────────────────────────
        val allSignals = RuntimeSignalRegistry.all()
        OrthosLogger.debug(
            "Registered runtime signals: %s",
            allSignals.joinToString { it.id.name }
        )

        val enabledSignals = allSignals.filter { signal ->
            val enabled =
                snapshot.signals[signal.id]?.enabled
                    ?: snapshot.groups[signal.type]?.enabled
                    ?: false

            OrthosLogger.trace(
                "Signal check: id=%s type=%s enabled=%s",
                signal.id,
                signal.type,
                enabled
            )

            enabled
        }

        OrthosLogger.info(
            "Enabled signals (%d): %s",
            enabledSignals.size,
            enabledSignals.joinToString { it.id.name }
        )

        // ──────────────────────────────
        // Execution
        // ──────────────────────────────
        val rawResults =
            signalExecutor.execute(enabledSignals, context)

        OrthosLogger.debug("Raw signal results:")
        rawResults.forEach {
            OrthosLogger.debug(
                "→ %s (%s): triggered=%s confidence=%f",
                it.signalId,
                it.signalType,
                it.triggered,
                it.confidence
            )
        }

        // ──────────────────────────────
        // Weight application
        // ──────────────────────────────
        val weightedResults =
            weightApplier.apply(rawResults, snapshot)

        OrthosLogger.debug("Weighted signal results:")
        weightedResults.forEach {
            OrthosLogger.debug(
                "→ %s: confidence=%f",
                it.signalId,
                it.confidence
            )
        }

        // ──────────────────────────────
        // Evidence filtering
        // ──────────────────────────────
        val evidences = weightedResults.filter { it.triggered }

        OrthosLogger.info(
            "Evidences (%d): %s",
            evidences.size,
            evidences.joinToString { it.signalId.name }
        )

        // ──────────────────────────────
        // Policy evaluation
        // ──────────────────────────────
        OrthosLogger.info("Evaluating policy")
        val verdict = policyEvaluator.evaluate(
            policyDefinition,
            evidences
        )

        OrthosLogger.info(
            "🧾 Verdict: state=%s score=%d",
            verdict.state,
            verdict.score
        )

        return verdict
    }

    // ──────────────────────────────
    // Builder
    // ──────────────────────────────
    class Builder(private val context: Context) {

        private val repository: FeatureRepository =
            FeatureRepositoryFactory.create(context)

        private var policyEvaluator: PolicyEvaluator =
            PolicyEvaluator()

        private var snapshotProvider: FeatureSnapshotProvider =
            DefaultFeatureSnapshotProvider(repository)

        private var identityProvider: RuntimeIdentityProvider =
            DefaultRuntimeIdentityProvider(context, InstallIdProvider(context))

        private var signalExecutor: SignalExecutor =
            DefaultSignalExecutor()

        private var weightApplier: SignalWeightApplier =
            SignalWeightApplier()

        private var failSafeHandler: FailSafeHandler =
            ConservativeFailSafeHandler()

        private var clock: Clock = SystemClock()

        fun withFailSafeHandler(handler: FailSafeHandler) = apply {
            this.failSafeHandler = handler
        }

        fun withIdentityProvider(provider: RuntimeIdentityProvider) = apply {
            this.identityProvider = provider
        }

        fun withFeatureSnapshotProvider(provider: FeatureSnapshotProvider) = apply {
            this.snapshotProvider = provider
        }

        fun withSignalExecutor(executor: SignalExecutor) = apply {
            this.signalExecutor = executor
        }

        fun withClock(clock: Clock) = apply {
            this.clock = clock
        }

        fun build(): OrthosRuntime {
            OrthosLogger.debug("OrthosRuntime built")
            return OrthosRuntime(
                applicationContext = context.applicationContext,
                identityProvider = identityProvider,
                snapshotProvider = snapshotProvider,
                signalExecutor = signalExecutor,
                policyEvaluator = policyEvaluator,
                weightApplier = weightApplier,
                failSafeHandler = failSafeHandler,
                clock = clock
            )
        }
    }
}
