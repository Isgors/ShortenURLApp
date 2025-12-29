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
import dev.igordesouza.orthos.runtime.policy.ConservativeFailSafeHandler
import dev.igordesouza.orthos.runtime.policy.PolicyEvaluator
import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.runtime.time.Clock
import dev.igordesouza.orthos.runtime.time.SystemClock

class OrthosRuntime private constructor(
    private val applicationContext: Context,
    private val signals: List<Signal>,
    private val identityProvider: RuntimeIdentityProvider,
    private val snapshotProvider: FeatureSnapshotProvider,
    private val signalExecutor: SignalExecutor,
    private val policyEvaluator: PolicyEvaluator,
    private val clock: Clock
) {

    suspend fun evaluate(): OrthosVerdict {
        val identity = identityProvider.provide()
        val snapshot = snapshotProvider.get(identity)

        val context = RuntimeSignalContext(
            applicationContext = applicationContext,
            identity = identity,
            clock = clock
        )

        val results = signalExecutor.execute(signals, context)

        return policyEvaluator.evaluate(
            policy = snapshot.policy,
            results = results
        )
    }


    class Builder(private val context: Context) {

        private var signals: List<Signal> = emptyList()

        private val repository: FeatureRepository = FeatureRepositoryFactory.create(context)
        private var snapshotProvider: FeatureSnapshotProvider =
            DefaultFeatureSnapshotProvider(repository)
        private var identityProvider: RuntimeIdentityProvider =
            DefaultRuntimeIdentityProvider(context, InstallIdProvider(context))
        private var signalExecutor: SignalExecutor = DefaultSignalExecutor()
        private var policyEvaluator: PolicyEvaluator =
            PolicyEvaluator(
                failSafeHandler = ConservativeFailSafeHandler()
            )

        private var clock: Clock = SystemClock()

        fun withSignals(signals: List<Signal>) = apply {
            this.signals = signals
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
            return OrthosRuntime(
                applicationContext = context.applicationContext,
                signals = signals,
                identityProvider = identityProvider,
                snapshotProvider = snapshotProvider,
                signalExecutor = signalExecutor,
                policyEvaluator = policyEvaluator,
                clock = clock
            )
        }
    }
}