/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.leon.su.R
import com.leon.su.databinding.FragmentListProductInvoicesBinding
import com.leon.su.domain.Product
import com.leon.su.presentation.adapter.ProductInvoicesAdapter
import com.leon.su.presentation.observer.ProductObserver
import com.leon.su.presentation.viewmodel.InvoicesActivityViewModel
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductInvoicesFragment :
    Fragment(R.layout.fragment_list_product_invoices),
    SwipeRefreshLayout.OnRefreshListener,
    OnItemChildClickListener,
    ProductObserver.Interfaces {

    private val mType: String by bundle(TYPE_KEY, Product.Type.MAKANAN.value)
    private val mBinding by viewBinding(FragmentListProductInvoicesBinding::bind)
    private val mProduct = mutableListOf<Product.Cart>()
    private val isLoading = ObservableBoolean(true)
    private val mViewModel by viewModel<ProductViewModel>()
    private val mSharedViewModel by activityViewModels<InvoicesActivityViewModel>()
    private val mAdapter by lazy {
        ProductInvoicesAdapter(layoutInflater, mProduct).apply {
            setOnItemChildClickListener(this@ProductInvoicesFragment)
        }
    }
    private var mQuery = MutableLiveData("")
    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
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
        mQuery.observe(
            viewLifecycleOwner,
            {
                when {
                    it.isNullOrBlank() || it == "null" ->
                        mAdapter.reset(mSharedViewModel.mCartItem)
                    else -> mAdapter.filter(it, mSharedViewModel.mCartItem)
                }
            }
        )
        mBinding.etSearch.addTextChangedListener {
            mHandler.removeCallbacksAndMessages(null)
            mHandler.postDelayed(
                {
                    mQuery.postValue(it.toString())
                },
                500
            )
        }
        mBinding.initAdapter()
        onRefresh()
    }

    private fun FragmentListProductInvoicesBinding.initAdapter() {
        isLoading = this@ProductInvoicesFragment.isLoading
        rvContent.adapter = mAdapter
        swipeRefreshLayout.setOnRefreshListener(this@ProductInvoicesFragment)
    }

    private fun getType() = when (mType) {
        Product.Type.MAKANAN.value -> Product.Type.MAKANAN
        Product.Type.MINUMAN.value -> Product.Type.MINUMAN
        Product.Type.LAINNYA.value -> Product.Type.LAINNYA
        else -> Product.Type.ALL
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
        mAdapter.updateData(data, mSharedViewModel.mCartItem)
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

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (adapter is ProductInvoicesAdapter) {
            val selected = adapter.getItem(position)
            when (view.id) {
                R.id.tvPlusEcer -> if ((selected.quantity + 1) >
                    selected.product?.data?.stok ?: 0
                ) {
                    ToastUtils.showShort("Stok tidak cukup!")
                } else {
                    val item = selected.apply {
                        quantity++
                    }
                    mSharedViewModel.setNewQuantity(item)
                    adapter.setData(
                        position,
                        item
                    )
                }
                R.id.tvMinusEcer -> if (selected.quantity != 0) {
                    val item = selected.apply {
                        quantity--
                    }
                    mSharedViewModel.setNewQuantity(item)
                    adapter.setData(
                        position,
                        item
                    )
                }
                R.id.tvPlusGrosir -> if ((
                    selected.quantity + (
                        selected.product?.data?.grosirUnit
                            ?: 0
                        )
                    ) > selected.product?.data?.stok ?: 0
                ) {
                    ToastUtils.showShort("Stok tidak cukup!")
                } else {
                    val item = selected.apply {
                        quantity += selected.product?.data?.grosirUnit ?: 0
                    }
                    mSharedViewModel.setNewQuantity(item)
                    adapter.setData(
                        position,
                        item
                    )
                }
                R.id.tvMinusGrosir -> if ((
                    selected.quantity - (
                        selected.product?.data?.grosirUnit
                            ?: 0
                        )
                    ) >= 0
                ) {
                    val item = selected.apply {
                        quantity -= selected.product?.data?.grosirUnit ?: 0
                    }
                    mSharedViewModel.setNewQuantity(item)
                    adapter.setData(
                        position,
                        item
                    )
                }
            }
        }
    }
}
