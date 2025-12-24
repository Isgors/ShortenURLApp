package dev.igordesouza.shortenurlapp.domain.usecase

import dev.igordesouza.shortenurlapp.domain.model.ShortenUrlOutcome
import kotlinx.coroutines.flow.Flow

interface ShortenUrlUseCase {
    operator fun invoke(input: String): Flow<ShortenUrlOutcome>
}