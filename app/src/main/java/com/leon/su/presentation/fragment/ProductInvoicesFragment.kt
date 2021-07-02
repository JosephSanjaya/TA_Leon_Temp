/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.FragmentListProductBinding
import com.leon.su.domain.Product
import com.leon.su.presentation.ProductRegisterActivity.Companion.intentAddProduct
import com.leon.su.presentation.adapter.ProductInvoicesAdapter
import com.leon.su.presentation.adapter.ProductListAdapter
import com.leon.su.presentation.observer.ProductObserver
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductInvoicesFragment :
    Fragment(R.layout.fragment_list_product),
    SwipeRefreshLayout.OnRefreshListener,
    ProductObserver.Interfaces,
    View.OnClickListener {

    private val mType: String by bundle(TYPE_KEY, Product.Type.MAKANAN.value)
    private val mBinding by viewBinding(FragmentListProductBinding::bind)
    private val mProduct = mutableListOf<Product.Cart>()
    private val isLoading = ObservableBoolean(true)
    private val mViewModel by viewModel<ProductViewModel>()
    private val mAdapter by lazy {
        ProductInvoicesAdapter(layoutInflater, mProduct)
    }
    private val addActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                onRefresh()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycle.addObserver(
            ProductObserver(
                this, mViewModel, viewLifecycleOwner
            )
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.initAdapter()
        onRefresh()
    }

    private fun FragmentListProductBinding.initAdapter() {
        isLoading = this@ProductInvoicesFragment.isLoading
        rvContent.adapter = mAdapter
        swipeRefreshLayout.setOnRefreshListener(this@ProductInvoicesFragment)
        listener = this@ProductInvoicesFragment
    }

    private fun getType() = when (mType) {
        Product.Type.MAKANAN.value -> Product.Type.MAKANAN
        Product.Type.MINUMAN.value -> Product.Type.MINUMAN
        Product.Type.LAINNYA.value -> Product.Type.LAINNYA
        else -> Product.Type.ALL
    }

    override fun onClick(v: View?): Unit = with(mBinding) {
        when (v) {
            btnAdd -> {
                addActivity.launch(requireContext().intentAddProduct(getType()))
            }
        }
    }

    override fun onRefresh() {
        mViewModel.fetch(Product.Status.ACTIVE, getType())
    }

    override fun onFetchProductLoading() {
        isLoading.set(true)
        super.onFetchProductLoading()
    }

    override fun onFetchProductFailed(e: Throwable) {
        mBinding.swipeRefreshLayout.isRefreshing = false
        isLoading.set(false)
        ToastUtils.showShort(e.message)
        super.onFetchProductFailed(e)
    }

    override fun onFetchProductSuccess(data: List<Product.Response>) {
        mBinding.swipeRefreshLayout.isRefreshing = false
        mAdapter.updateData(data)
        isLoading.set(false)
        super.onFetchProductSuccess(data)
    }

    companion object {
        const val TYPE_KEY = "key"
        fun newInstance(type: Product.Type) = ProductInvoicesFragment().apply {
            arguments = intentOf {
                +(TYPE_KEY to type.value)
            }.extras
        }
    }
}
