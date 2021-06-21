package com.leon.su.di

import com.leon.su.data.ProductRepository
import com.leon.su.presentation.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ProductDI {
    val modules = module {
        single {
            ProductRepository()
        }
        viewModel {
            ProductViewModel(get())
        }
    }
}