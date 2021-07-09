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
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.firebase.storage.StorageReference
import com.leon.su.R
import com.leon.su.databinding.FragmentHistoryBinding
import com.leon.su.databinding.FragmentListProductBinding
import com.leon.su.presentation.adapter.HistoryListAdapter
import com.leon.su.presentation.observer.StorageObserver
import com.leon.su.presentation.viewmodel.HistoryActivityViewModel
import com.leon.su.presentation.viewmodel.StorageViewModel
import com.leon.su.utils.appCompatActivity
import com.leon.su.utils.replaceFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment :
    Fragment(R.layout.fragment_history),
    SwipeRefreshLayout.OnRefreshListener,
    StorageObserver.Interfaces,
    OnItemClickListener {

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
            setOnItemClickListener(this@HistoryFragment)
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
        isLoading = this@HistoryFragment.isLoading
        rvContent.adapter = mAdapter
        swipeRefreshLayout.setOnRefreshListener(this@HistoryFragment)
    }

    override fun onRefresh() {
        mViewModel.fetch()
    }

    override fun onFetchLoading() {
        super.onFetchLoading()
        isLoading.set(true)
    }

    override fun onFetchFailed(e: Throwable) {
        super.onFetchFailed(e)
        mBinding.swipeRefreshLayout.isRefreshing = false
        isLoading.set(false)
        ToastUtils.showShort(e.message)
    }

    override fun onFetchSuccess(data: MutableList<StorageReference>) {
        super.onFetchSuccess(data)
        mBinding.swipeRefreshLayout.isRefreshing = false
        mAdapter.updateData(data)
        isLoading.set(false)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (adapter is HistoryListAdapter) {
            val selected = adapter.getItem(position)
            mSharedViewModel.mSelectedReferences = selected
            appCompatActivity?.replaceFragment(
                HistoryDownloadFragment(), isBackstack = true, isAnimate = true
            )
        }
    }
}
