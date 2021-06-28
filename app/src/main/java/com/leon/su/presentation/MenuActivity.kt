package com.leon.su.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.ActivityMenuBinding
import com.leon.su.domain.ProductData
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuActivity :
    AppCompatActivity(R.layout.activity_menu),
    View.OnClickListener {

    private val mBinding by viewBinding(ActivityMenuBinding::bind)
    private val mProductViewModel by viewModel<ProductViewModel>()
    private val mProduct = ProductData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.listener = this
        stateObserve()
    }

    private fun stateObserve() {
        lifecycleScope.launch {
            mProductViewModel.mProduct.collect {
                when (it) {
                    is State.Idle -> {
                        // idle
                    }
                    is State.Loading -> {
                        ToastUtils.showShort("Saya sedang Loading!")
                    }
                    is State.Success -> {
                        ToastUtils.showShort("Product = ${it.data}")
                        mProductViewModel.resetProductState()
                        // success
                    }
                    is State.Failed -> {
                        ToastUtils.showShort("Error = ${it.throwable.message}")
                        mProductViewModel.resetProductState()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.ProductButton -> mProductViewModel.fetchData()
        }
    }
}
