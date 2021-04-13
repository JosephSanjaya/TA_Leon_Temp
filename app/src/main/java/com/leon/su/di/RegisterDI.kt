package com.leon.su.di

import com.leon.su.data.RegisterRepository
import com.leon.su.presentation.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

object RegisterDI {
    val modules = module {
        single {
            RegisterRepository()
        }
        viewModel {
            RegisterViewModel(get())
        }
    }
}