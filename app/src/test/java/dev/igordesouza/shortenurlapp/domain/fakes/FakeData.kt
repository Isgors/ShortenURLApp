package dev.igordesouza.shortenurlapp.domain.fakes

import dev.igordesouza.shortenurlapp.domain.model.Url

object FakeData {

    fun url(
        alias: String = "abc123",
        originalUrl: String = "https://google.com",
        shortenedUrl: String = "https://short.ly/abc123"
    ): Url =
        Url(
            alias = alias,
            originalUrl = originalUrl,
            shortenedUrl = shortenedUrl
        )

    fun urls(count: Int): List<Url> =
        (0 until count).map {
            url(alias = "alias_$it")
        }
}
