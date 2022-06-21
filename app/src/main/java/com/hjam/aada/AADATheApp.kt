package com.hjam.aada

import android.app.Application

class AADATheApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AADATheApp
            private set
    }
}