package com.leon.su.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leon.su.R
import com.leon.su.databinding.ActivityFragmentBinding
import com.leon.su.presentation.fragment.ProductInvoicesTabFragment
import com.leon.su.presentation.viewmodel.InvoicesActivityViewModel
import com.leon.su.utils.replaceFragment
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf

class ProductInvoicesActivity : AppCompatActivity(R.layout.activity_fragment) {
    private val mBinding by viewBinding(ActivityFragmentBinding::bind)
    private val mSharedViewModel by viewModels<InvoicesActivityViewModel>()
    private val isTambah by bundle(TAMBAH_KEY, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            title = if(isTambah)"Barang Masuk" else "Buat Invoice"
            setDisplayHomeAsUpEnabled(true)
        }
        replaceFragment(ProductInvoicesTabFragment.newInstance(isTambah), isAnimate = true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAMBAH_KEY = "tambah"
        fun Context.launchInvoices(
            isTambah: Boolean
        ) = intentOf<ProductInvoicesActivity> {
            putExtra(TAMBAH_KEY to isTambah)
            startActivity(this@launchInvoices)
        }
    }
}
