package com.leon.su.di

import com.leon.su.data.StorageRepository
import com.leon.su.presentation.viewmodel.StorageViewModel
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object StorageDI {
    val modules = module {
        single {
            Fetch.getInstance(
                FetchConfiguration.Builder(androidContext())
                    .setDownloadConcurrentLimit(3)
                    .build()
            )
        }
        single {
            StorageRepository(get(), androidContext(), get())
        }
        viewModel {
            StorageViewModel(get())
        }
    }
}
