package com.leon.su

import android.app.Application
import com.leon.su.di.RegisterDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

class SUApplication : Application() {

    override fun onCreate() {
        startKoin {
            androidLogger()
            androidContext(this@SUApplication)
            modules(
                RegisterDI.modules
            )
        }
        super.onCreate()
    }
}
