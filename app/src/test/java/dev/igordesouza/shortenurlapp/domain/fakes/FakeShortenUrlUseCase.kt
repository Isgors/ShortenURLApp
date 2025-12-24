package dev.igordesouza.shortenurlapp.domain.fakes

import dev.igordesouza.shortenurlapp.domain.model.ShortenUrlOutcome
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeShortenUrlUseCase : ShortenUrlUseCase {

    private var outcome: ShortenUrlOutcome =
        ShortenUrlOutcome.EmptyInput

    fun emit(outcome: ShortenUrlOutcome) {
        this.outcome = outcome
    }

    override fun invoke(input: String): Flow<ShortenUrlOutcome> =
        flowOf(outcome)
}
