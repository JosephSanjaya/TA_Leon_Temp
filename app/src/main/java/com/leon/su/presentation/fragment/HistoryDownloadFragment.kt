/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.firebase.storage.StorageReference
import com.leon.su.BuildConfig
import com.leon.su.R
import com.leon.su.databinding.FragmentHistoryBinding
import com.leon.su.presentation.adapter.HistoryListAdapter
import com.leon.su.presentation.observer.StorageObserver
import com.leon.su.presentation.viewmodel.HistoryActivityViewModel
import com.leon.su.presentation.viewmodel.StorageViewModel
import com.leon.su.utils.makeLoadingDialog
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryDownloadFragment :
    Fragment(R.layout.fragment_history),
    SwipeRefreshLayout.OnRefreshListener,
    StorageObserver.Interfaces,
    OnItemClickListener {
    private val loading by lazy {
        context?.makeLoadingDialog(false)
    }
    private val mBinding by viewBinding(FragmentHistoryBinding::bind)
    private val mRefList = mutableListOf<StorageReference>()
    private val isLoading = ObservableBoolean(true)
    private val mViewModel by viewModel<StorageViewModel>()
    private val mSharedViewModel by activityViewModels<HistoryActivityViewModel>()
    private var mQuery = MutableLiveData("")
    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val mAdapter by lazy {
        HistoryListAdapter(layoutInflater, mRefList).apply {
            setOnItemClickListener(this@HistoryDownloadFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycle.addObserver(
            StorageObserver(
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
                        mAdapter.reset()
                    else -> mAdapter.filter(it)
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

    private fun FragmentHistoryBinding.initAdapter() {
        isLoading = this@HistoryDownloadFragment.isLoading
        rvContent.adapter = mAdapter
        swipeRefreshLayout.setOnRefreshListener(this@HistoryDownloadFragment)
    }

    override fun onRefresh() {
        mViewModel.fetchByRef(mSharedViewModel.mSelectedReferences!!)
    }

    override fun onFetchByRefLoading() {
        super.onFetchLoading()
        isLoading.set(true)
    }

    override fun onFetchByRefFailed(e: Throwable) {
        super.onFetchFailed(e)
        mBinding.swipeRefreshLayout.isRefreshing = false
        isLoading.set(false)
        ToastUtils.showShort(e.message)
    }

    override fun onFetchByRefSuccess(data: MutableList<StorageReference>) {
        super.onFetchSuccess(data)
        mBinding.swipeRefreshLayout.isRefreshing = false
        mAdapter.updateData(data)
        isLoading.set(false)
    }

    override fun onDownloadLoading() {
        super.onDownloadLoading()
        loading?.show()
    }

    override fun onDownloadFailed(e: Throwable) {
        super.onDownloadFailed(e)
        loading?.dismiss()
        ToastUtils.showShort(e.message)
    }

    override fun onDownloadSuccess(data: File) {
        super.onDownloadSuccess(data)
        loading?.dismiss()
        try {
            val viewFile = Intent(Intent.ACTION_VIEW)
            val map = MimeTypeMap.getSingleton()
            val ext = FileUtils.getFileExtension(data)
            var type = map.getMimeTypeFromExtension(ext)
            if (type == null) type = "*/*"
            val uri = FileProvider.getUriForFile(
                requireActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                data
            )
            viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            viewFile.setDataAndType(uri, type)
            startActivity(viewFile)
        } catch (e: Throwable) {
            ToastUtils.showShort(e.message)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (adapter is HistoryListAdapter) {
            PermissionUtils.permission(
                PermissionConstants.STORAGE
            ).callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    val selected = adapter.getItem(position)
                    mViewModel.download(selected)
                }

                override fun onDenied() {
                    ToastUtils.showShort("Mohon berikan ijin terlebih dahulu!")
                }
            }).request()
        }
    }
}
