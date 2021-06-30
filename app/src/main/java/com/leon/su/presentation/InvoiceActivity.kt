package com.leon.su.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.leon.su.R
import com.leon.su.databinding.ActivityInvoiceBinding
import com.leon.su.presentation.adapter.Adapter

class InvoiceActivity : AppCompatActivity() {
    private val mBinding by viewBinding(ActivityInvoiceBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        var adapter = Adapter(supportFragmentManager, lifecycle)

        mBinding.viewPager.adapter = adapter
//        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager, object : TabLayoutMediator.OnConfigureTabCallback{
//            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                when(position)
//                {
//                    0->{tab.text = "Makanan"}
//                    1->{tab.text = "Minuman"}
//                    2->{tab.text = "Lainnya"}
//                }
//            }
//        }).attach()
    }
}