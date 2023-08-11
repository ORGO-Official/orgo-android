package com.orgo.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OrgoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}

