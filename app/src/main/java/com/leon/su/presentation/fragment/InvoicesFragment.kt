/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.SuccessResponse
import com.leon.su.R
import com.leon.su.data.ProductRepository
import com.leon.su.data.users
import com.leon.su.databinding.FragmentInvoicesBinding
import com.leon.su.presentation.adapter.InvoicesListAdapter
import com.leon.su.presentation.adapter.InvoicesListProvider
import com.leon.su.presentation.observer.ProductObserver
import com.leon.su.presentation.viewmodel.InvoicesActivityViewModel
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.leon.su.utils.makeLoadingDialog
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class InvoicesFragment :
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
        mBinding.rvContent.adapter = mAdapter
        mBinding.listener = this
        mViewModel.sold(mSharedViewModel.mCartItem)
    }

    private fun generatePDF() {
        PdfGenerator.getBuilder().setContext(requireContext())
            .fromViewSource()
            .fromView(mBinding.nsvContent)
            .setFileName(DateTime.now().format(DateFormat.DEFAULT_FORMAT))
            .setFolderName(PathUtils.getInternalAppDataPath())
            .openPDFafterGeneration(true)
            .build(object : PdfGeneratorListener() {
                override fun onSuccess(response: SuccessResponse?) {
                    super.onSuccess(response)
                    try {
                        val viewFile = Intent(Intent.ACTION_VIEW)
                        val map = MimeTypeMap.getSingleton()
                        val ext = MimeTypeMap.getFileExtensionFromUrl(response?.file?.name)
                        var type = map.getMimeTypeFromExtension(ext)
                        if (type == null) type = "*/*"
                        viewFile.setDataAndType(UriUtils.file2Uri(response?.file), type)
                        startActivity(viewFile)
                    } catch (e: Throwable) {
                        ToastUtils.showShort(e.message)
                    }
                }

                override fun onStartPDFGeneration() {
                    loading?.show()
                }

                override fun onFinishPDFGeneration() {
                    loading?.dismiss()
                }
            })
    }

    override fun onSoldProductLoading() {
        super.onSoldProductLoading()
        loading?.show()
    }

    override fun onSoldProductFailed(e: Throwable) {
        super.onSoldProductFailed(e)
        loading?.dismiss()
        ToastUtils.showShort(e.message)
    }

    override fun onSoldProductSuccess() {
        super.onSoldProductSuccess()
        val data = mutableListOf<InvoicesListProvider.Type>()
        data.add(
            InvoicesListProvider.Type.Header(
                DateTime.now(),
                mSharedPreferences.users?.data?.nama.toString()
            )
        )
        data.addAll(
            mSharedViewModel.mCartItem.map {
                InvoicesListProvider.Type.Invoices(it)
            }
        )
        data.add(
            InvoicesListProvider.Type.Total(
                mSharedViewModel.mCartItem.sumOf {
                    it.getTotal()
                }
            )
        )
        mAdapter.setNewInstance(data)
        loading?.dismiss()
    }

    override fun onClick(v: View?): Unit = with(mBinding) {
        when (v) {
            btnExport -> PermissionUtils.permission(
                PermissionConstants.STORAGE
            ).callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    generatePDF()
                }

                override fun onDenied() {
                    ToastUtils.showShort("Mohon berikan ijin terlebih dahulu!")
                }
            }).request()
        }
    }
}
