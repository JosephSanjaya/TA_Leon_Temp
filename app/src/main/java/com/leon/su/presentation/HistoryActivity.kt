/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leon.su.R
import com.leon.su.databinding.ActivityFragmentBinding
import com.leon.su.presentation.fragment.HistoryFragment
import com.leon.su.presentation.viewmodel.HistoryActivityViewModel
import com.leon.su.utils.replaceFragment

class HistoryActivity : AppCompatActivity(R.layout.activity_fragment) {
    private val mBinding by viewBinding(ActivityFragmentBinding::bind)
    private val mSharedViewModel by viewModels<HistoryActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            title = "Riwayat Transaksi"
            setDisplayHomeAsUpEnabled(true)
        }
        replaceFragment(HistoryFragment())
    }
}
