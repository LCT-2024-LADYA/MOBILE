package ru.gozerov.fitladya.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FitLadyaApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}