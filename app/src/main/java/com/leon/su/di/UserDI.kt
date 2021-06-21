package com.leon.su.di

import com.leon.su.data.UserRepository
import com.leon.su.presentation.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UserDI {
    val modules = module {
        single {
            UserRepository()
        }
        viewModel {
            UserViewModel(get())
        }
    }
}