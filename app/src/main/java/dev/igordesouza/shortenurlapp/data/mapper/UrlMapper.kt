package dev.igordesouza.shortenurlapp.data.mapper

import dev.igordesouza.shortenurlapp.data.local.model.UrlEntity
import dev.igordesouza.shortenurlapp.domain.model.Url

fun Url.toEntity(): UrlEntity {
    return UrlEntity(
        alias = alias,
        originalUrl = originalUrl,
        shortenedUrl = shortenedUrl
    )
}

fun UrlEntity.toDomain(): Url {
    return Url(
        alias = alias,
        originalUrl = originalUrl,
        shortenedUrl = shortenedUrl
    )
}

fun List<UrlEntity>.toDomain(): List<Url> {
    return map { it.toDomain() }
}
