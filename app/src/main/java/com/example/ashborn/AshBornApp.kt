package com.example.ashborn

import android.app.Application
import com.example.ashborn.data.AppContainer
import com.example.ashborn.data.AppDataContainer

class AshBornApp:Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}