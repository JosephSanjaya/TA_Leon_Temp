package com.leon.su.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.leon.su.R
import com.leon.su.databinding.ActivityTabBinding
import com.leon.su.domain.Product
import com.leon.su.presentation.fragment.ProductListFragment

class EditProductActivity : AppCompatActivity(R.layout.activity_tab) {
    private val mBinding by viewBinding(ActivityTabBinding::bind)
    private var mTabLayoutMediator: TabLayoutMediator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            title = "Edit Produk"
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.setupBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun ActivityTabBinding.setupBinding() {
        vpContent.apply {
            adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> ProductListFragment.newInstance(Product.Type.MAKANAN, true)
                        1 -> ProductListFragment.newInstance(Product.Type.MINUMAN, true)
                        else -> ProductListFragment.newInstance(Product.Type.LAINNYA, true)
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
    }

    override fun onDestroy() {
        mTabLayoutMediator?.detach()
        mTabLayoutMediator = null
        super.onDestroy()
    }
}
