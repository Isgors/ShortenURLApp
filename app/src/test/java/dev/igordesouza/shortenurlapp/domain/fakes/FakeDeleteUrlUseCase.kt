package dev.igordesouza.shortenurlapp.domain.fakes

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase

class FakeDeleteUrlUseCase : DeleteUrlUseCase {

    val deletedUrls = mutableListOf<Url>()

    override suspend fun invoke(url: Url) {
        deletedUrls += url
    }
}
