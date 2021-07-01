package com.leon.su.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.ActivityProductRegisterBinding
import com.leon.su.domain.Product
import com.leon.su.presentation.observer.ProductObserver
import com.leon.su.presentation.viewmodel.ProductViewModel
import com.leon.su.utils.makeLoadingDialog
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductRegisterActivity :
    AppCompatActivity(R.layout.activity_product_register),
    View.OnClickListener,
    ProductObserver.Interfaces {

    private val mType: String by bundle(TYPE_KEY, Product.Type.MAKANAN.value)
    private val mBinding by viewBinding(ActivityProductRegisterBinding::bind)
    private val mLoading by lazy {
        makeLoadingDialog(false)
    }
    private val mViewModel by viewModel<ProductViewModel>()
    private val mForm by lazy {
        mutableListOf(
            mBinding.etCapital,
            mBinding.etGrosirUnit,
            mBinding.etProductName,
            mBinding.etRetail,
            mBinding.etStock,
            mBinding.etWholesale
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(
            ProductObserver(
                this, mViewModel, this
            )
        )
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            title = "Register Produk ${StringUtils.upperFirstLetter(mType)}"
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.listener = this
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun validate(): Boolean {
        return !mForm.any {
            it.text.isNullOrBlank()
        }
    }

    override fun onClick(v: View?): Unit = with(mBinding) {
        when (v) {
            btnAdd -> if (validate()) {
                mViewModel.insert(
                    Product.Data(
                        namaProduct = mBinding.etProductName.text.toString(),
                        hargaModal = mBinding.etCapital.text.toString().toDoubleOrNull() ?: 0.0,
                        hargaEcer = mBinding.etRetail.text.toString().toDoubleOrNull() ?: 0.0,
                        hargaGrosir = mBinding.etWholesale.text.toString().toDoubleOrNull() ?: 0.0,
                        grosirUnit = mBinding.etGrosirUnit.text.toString().toIntOrNull() ?: 0,
                        stok = mBinding.etStock.text.toString().toIntOrNull() ?: 0,
                        status = Product.Status.ACTIVE.value,
                        type = mType
                    )
                )
            } else {
                ToastUtils.showShort("Mohon mengisi semua form!")
            }
        }
    }

    override fun onBuatProductLoading() {
        super.onBuatProductLoading()
        mLoading.show()
    }

    override fun onBuatProductFailed(e: Throwable) {
        mLoading.dismiss()
        ToastUtils.showShort(e.message)
        super.onBuatProductFailed(e)
    }

    override fun onBuatProductSuccess(added: Product.Response?) {
        mLoading.dismiss()
        ToastUtils.showShort("Berhasil menambahkan produk!")
        setResult(Activity.RESULT_OK)
        finish()
        super.onBuatProductSuccess(added)
    }

    companion object {
        const val TYPE_KEY = "type"
        fun Context.intentAddProduct(type: Product.Type) = intentOf<ProductRegisterActivity> {
            putExtra(TYPE_KEY to type.value)
        }
    }
}
