package ru.gozerov.domain.models

data class CheckTokenResult(
    val isAuth: Boolean,
    val isClient: Boolean
)
