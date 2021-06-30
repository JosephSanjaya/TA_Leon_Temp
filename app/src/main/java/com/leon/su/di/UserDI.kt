package com.leon.su.di

import android.content.Context
import com.leon.su.data.PasswordRepository
import com.leon.su.data.RegisterRepository
import com.leon.su.data.UserRepository
import com.leon.su.data.VerifyRepository
import com.leon.su.presentation.viewmodel.PasswordViewModel
import com.leon.su.presentation.viewmodel.RegisterViewModel
import com.leon.su.presentation.viewmodel.UserViewModel
import com.leon.su.presentation.viewmodel.VerifyViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object UserDI {
    private const val PREFERENCES_NAME = "su"
    val modules = module {
        single {
            androidApplication().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
        single {
            UserRepository(get())
        }
        viewModel {
            UserViewModel(get())
        }
        single {
            PasswordRepository()
        }
        viewModel {
            PasswordViewModel(get())
        }
        single {
            RegisterRepository()
        }
        viewModel {
            RegisterViewModel(get())
        }
        single {
            VerifyRepository()
        }
        viewModel {
            VerifyViewModel(get())
        }
    }
}
