package com.leon.su

import android.app.Application
import com.leon.su.di.ProductDI
import com.leon.su.di.UserDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SUApplication : Application() {

    override fun onCreate() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SUApplication)
            modules(
                UserDI.modules,
                ProductDI.modules
            )
        }
        super.onCreate()
    }
}
