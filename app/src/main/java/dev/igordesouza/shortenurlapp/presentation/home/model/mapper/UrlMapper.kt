package dev.igordesouza.shortenurlapp.presentation.home.model.mapper

import dev.igordesouza.shortenurlapp.domain.model.Url as DomainUrl
import dev.igordesouza.shortenurlapp.presentation.home.model.Url as PresentationUrl

fun DomainUrl.toPresentation(): PresentationUrl {
    return PresentationUrl(
        alias = alias,
        originalUrl = originalUrl,
        shortenedUrl = shortenedUrl
    )
}

fun PresentationUrl.toDomain(): DomainUrl {
    return DomainUrl(
        alias = alias,
        originalUrl = originalUrl,
        shortenedUrl = shortenedUrl
    )
}