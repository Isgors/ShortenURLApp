package dev.igordesouza.shortenurlapp.domain.model

data class Url(
    val alias: String,
    val originalUrl: String,
    val shortenedUrl: String
)
