package com.leon.su.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.leon.su.R
import com.leon.su.databinding.FragmentListProductBinding
import com.leon.su.domain.Product
import com.leon.su.presentation.EditFormProductActivity.Companion.intentEditProduct
import com.leon.su.presentation.ProductRegisterActivity.Companion.intentAddProduct
import com.leon.su.presentation.adapter.ProductListAdapter
import com.leon.su.presentation.observer.ProductObserver
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment :
    Fragment(R.layout.fragment_list_product),
    SwipeRefreshLayout.OnRefreshListener,
    ProductObserver.Interfaces,
    OnItemClickListener,
    View.OnClickListener {

    private val mType: String by bundle(TYPE_KEY, Product.Type.MAKANAN.value)
    private val isEdit: Boolean by bundle(EDIT_KEY, false)
    private val mBinding by viewBinding(FragmentListProductBinding::bind)
    private val mProduct = mutableListOf<Product.Response>()
    private val isLoading = ObservableBoolean(true)
    private val mViewModel by viewModel<ProductViewModel>()
    private var mQuery = MutableLiveData("")
    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val mAdapter by lazy {
        ProductListAdapter(layoutInflater, mProduct).apply {
            if (isEdit) setOnItemClickListener(this@ProductListFragment)
        }
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
        mBinding.btnAdd.isGone = isEdit
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

    private fun FragmentListProductBinding.initAdapter() {
        isLoading = this@ProductListFragment.isLoading
        rvContent.adapter = mAdapter
        swipeRefreshLayout.setOnRefreshListener(this@ProductListFragment)
        listener = this@ProductListFragment
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
        mViewModel.fetchAll(getType())
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
        const val EDIT_KEY = "edit"
        fun newInstance(type: Product.Type, isEdit: Boolean = false) = ProductListFragment().apply {
            arguments = intentOf {
                +(TYPE_KEY to type.value)
                +(EDIT_KEY to isEdit)
            }.extras
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (adapter is ProductListAdapter) {
            val selected = adapter.getItem(position)
            addActivity.launch(requireContext().intentEditProduct(selected.id.toString()))
        }
    }
}
