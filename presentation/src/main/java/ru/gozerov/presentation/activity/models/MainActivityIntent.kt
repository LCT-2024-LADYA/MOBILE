package ru.gozerov.presentation.activity.models

sealed interface MainActivityIntent {

    object CheckAuth : MainActivityIntent

}