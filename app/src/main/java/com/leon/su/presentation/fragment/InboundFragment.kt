package com.leon.su.presentation.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.FragmentInvoicesBinding
import com.leon.su.presentation.adapter.InvoicesListAdapter
import com.leon.su.presentation.adapter.InvoicesListProvider
import com.leon.su.presentation.observer.ProductObserver
import com.leon.su.presentation.viewmodel.InvoicesActivityViewModel
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.leon.su.utils.makeLoadingDialog
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class InboundFragment :
    Fragment(R.layout.fragment_invoices),
    View.OnClickListener, ProductObserver.Interfaces {

    private val mBinding by viewBinding(FragmentInvoicesBinding::bind)
    private val mSharedViewModel by activityViewModels<InvoicesActivityViewModel>()
    private val mViewModel by viewModel<ProductViewModel>()
    private val mSharedPreferences by inject<SharedPreferences>()
    private val mData = mutableListOf<InvoicesListProvider.Type>()
    private val loading by lazy { context?.makeLoadingDialog(false) }
    private val mAdapter by lazy {
        InvoicesListAdapter(mData)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycle.addObserver(
            ProductObserver(this, mViewModel, viewLifecycleOwner)
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnExport.text = "Konfirmasi"
        mBinding.rvContent.adapter = mAdapter
        mBinding.listener = this
        val data = mutableListOf<InvoicesListProvider.Type>()
        data.addAll(
            mSharedViewModel.mCartItem.map {
                InvoicesListProvider.Type.Inbound(it)
            }
        )
        mAdapter.setNewInstance(data)
    }


    override fun onAddProductLoading() {
        super.onAddProductLoading()
        loading?.show()
    }

    override fun onAddProductFailed(e: Throwable) {
        super.onAddProductFailed(e)
        loading?.dismiss()
        ToastUtils.showShort(e.message)
    }

    override fun onAddProductSuccess() {
        super.onAddProductSuccess()
        loading?.dismiss()
        ToastUtils.showShort("Stok berhasil ditambahkan")
        activity?.finish()
    }

    override fun onClick(v: View?): Unit = with(mBinding) {
        when (v) {
            btnExport -> PermissionUtils.permission(
                PermissionConstants.STORAGE
            ).callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    mViewModel.add(mSharedViewModel.mCartItem)
                }

                override fun onDenied() {
                    ToastUtils.showShort("Mohon berikan ijin terlebih dahulu!")
                }
            }).request()
        }
    }
}
