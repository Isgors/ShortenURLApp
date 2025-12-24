package dev.igordesouza.shortenurlapp.domain.fakes

import dev.igordesouza.shortenurlapp.domain.model.Url
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase

class FakeDeleteAllUrlsUseCase : DeleteAllUrlsUseCase {

    var wasCalled = false

    override suspend fun invoke() {
        wasCalled = true
    }
}
