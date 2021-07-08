package com.leon.su.di

import com.leon.su.data.ProductRepository
import com.leon.su.data.StorageRepository
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.leon.su.presentation.viewmodel.StorageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object StorageDI {
    val modules = module {
        single {
            StorageRepository(get())
        }
        viewModel {
            StorageViewModel(get())
        }
    }
}