package com.leon.su.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leon.su.R
import com.leon.su.databinding.ActivityMainBinding
import com.leon.su.domain.Product
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuActivity:
    AppCompatActivity(R.layout.activity_menu),
    View.OnClickListener {

    private val mBinding by viewBinding(ActivityMainBinding::bind)
    private val mProductViewModel by viewModel<ProductViewModel>()
    private val mProduct = Product()

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
                        // loading
                    }
                    is State.Success -> {
                        mProductViewModel.resetProductState()
                        // success
                    }
                    is State.Failed -> {
                        mProductViewModel.resetProductState()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}