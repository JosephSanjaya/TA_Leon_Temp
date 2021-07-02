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
import com.leon.su.presentation.fragment.ProductInvoicesTabFragment
import com.leon.su.presentation.viewmodel.InvoicesActivityViewModel
import com.leon.su.utils.replaceFragment

class ProductInvoicesActivity : AppCompatActivity(R.layout.activity_fragment) {
    private val mBinding by viewBinding(ActivityFragmentBinding::bind)
    private val mSharedViewModel by viewModels<InvoicesActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            title = "Buat Invoices"
            setDisplayHomeAsUpEnabled(true)
        }
        replaceFragment(ProductInvoicesTabFragment(), isAnimate = true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
