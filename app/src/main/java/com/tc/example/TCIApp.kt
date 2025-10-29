package com.tc.example

import android.app.Application
import com.tc.example.data.di.dataModule
import com.tc.example.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TCIApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
                androidContext(this@TCIApp)
            modules(
                dataModule,
                appModule
            )
        }
    }
}