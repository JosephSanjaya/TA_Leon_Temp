package com.leon.su.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.leon.su.R
import com.leon.su.databinding.FragmentTabInvoicesBinding
import com.leon.su.domain.Product
import com.leon.su.presentation.viewmodel.InvoicesActivityViewModel
import com.leon.su.utils.appCompatActivity
import com.leon.su.utils.replaceFragment
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf

class ProductInvoicesTabFragment : Fragment(R.layout.fragment_tab_invoices), View.OnClickListener {
    private val mBinding by viewBinding(FragmentTabInvoicesBinding::bind)
    private var mTabLayoutMediator: TabLayoutMediator? = null
    private val isTambah: Boolean by bundle(TAMBAH_KEY, false)
    private val mSharedViewModel by activityViewModels<InvoicesActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.setupBinding()
    }

    override fun onDestroyView() {
        mTabLayoutMediator?.detach()
        mTabLayoutMediator = null
        super.onDestroyView()
    }

    private fun FragmentTabInvoicesBinding.setupBinding() {
        if(isTambah){
            btnCreateInvoices.text = "Tambah Stok"
        }
        vpContent.apply {
            adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> ProductInvoicesFragment.newInstance(Product.Type.MAKANAN, isTambah)
                        1 -> ProductInvoicesFragment.newInstance(Product.Type.MINUMAN, isTambah)
                        else -> ProductInvoicesFragment.newInstance(Product.Type.LAINNYA, isTambah)
                    }
                }

                override fun getItemCount(): Int {
                    return 3
                }
            }
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewDetachedFromWindow(p0: View?) {
                    adapter = null
                }

                override fun onViewAttachedToWindow(p0: View?) {}
            })
            offscreenPageLimit = 3
        }
        mTabLayoutMediator =
            TabLayoutMediator(mBinding.tlLayout, mBinding.vpContent) { tab, position ->
                tab.text = when (position) {
                    0 -> "Makanan"
                    1 -> "Minuman"
                    else -> "Lainnya"
                }
            }
        mTabLayoutMediator?.attach()
        listener = this@ProductInvoicesTabFragment
    }

    override fun onClick(v: View?): Unit = with(mBinding) {
        when (v) {
            btnCreateInvoices -> if (mSharedViewModel.mCartItem.isNullOrEmpty()) {
                ToastUtils.showShort("Mohon mengisi produk terlebih dahulu!")
            } else {
                if (isTambah) {
                    appCompatActivity?.replaceFragment(
                        InboundFragment(),
                        isAnimate = true,
                        isBackstack = true,
                        isInclusive = true
                    )
                } else {
                    appCompatActivity?.replaceFragment(
                        InvoicesFragment(),
                        isAnimate = true,
                        isBackstack = true,
                        isInclusive = true
                    )
                }
            }
        }
    }

    companion object {
        const val TAMBAH_KEY = "tambah"
        fun newInstance(isTambah: Boolean = false) = ProductInvoicesTabFragment().apply {
            arguments = intentOf {
                +(TAMBAH_KEY to isTambah)
            }.extras
        }
    }
}
