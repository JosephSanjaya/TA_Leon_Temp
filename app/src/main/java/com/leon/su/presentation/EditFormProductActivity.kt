package com.leon.su.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
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

class EditFormProductActivity :
    AppCompatActivity(R.layout.activity_product_register),
    View.OnClickListener,
    ProductObserver.Interfaces {

    private val mId: String by bundle(ID_KEY, Product.Type.MAKANAN.value)
    private val mBinding by viewBinding(ActivityProductRegisterBinding::bind)
    private val mLoading by lazy {
        makeLoadingDialog(false)
    }
    private val mViewModel by viewModel<ProductViewModel>()
    private var mData: Product.Response? = null
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
            title = "Edit Produk"
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.tbStatus.visibility = View.VISIBLE
        mBinding.listener = this
        mBinding.etStock.isEnabled = false
        mBinding.etProductName.isEnabled = false
        mBinding.etGrosirUnit.isEnabled = false
        mViewModel.getById(mId)
    }
    
    private fun Product.Response.setForm() = with(mBinding) {
        mData = this@setForm
        etProductName.setText(data?.namaProduct)
        etCapital.setText(data?.hargaModal?.toInt()?.toString())
        etGrosirUnit.setText(data?.grosirUnit?.toString())
        etRetail.setText(data?.hargaEcer?.toString())
        etStock.setText(data?.stok?.toString())
        etWholesale.setText(data?.hargaGrosir?.toInt()?.toString())
        tbStatus.isChecked = data?.status == Product.Status.ACTIVE.value
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

    override fun onGetByIdLoading() {
        mLoading.show()
        super.onGetByIdLoading()
    }

    override fun onGetByIdFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        mLoading.dismiss()
        finish()
        super.onGetByIdFailed(e)
    }

    override fun onGetByIdSuccess(data: Product.Response) {
        data.setForm()
        mLoading.dismiss()
        super.onGetByIdSuccess(data)
    }

    override fun onClick(v: View?): Unit = with(mBinding) {
        when (v) {
            btnAdd -> if (validate()) {
                mViewModel.edit(Product.Response(
                    id = mData?.id,
                    data = Product.Data(
                        namaProduct = mBinding.etProductName.text.toString(),
                        hargaModal = mBinding.etCapital.text.toString().toDoubleOrNull() ?: 0.0,
                        hargaEcer = mBinding.etRetail.text.toString().toDoubleOrNull() ?: 0.0,
                        hargaGrosir = mBinding.etWholesale.text.toString().toDoubleOrNull() ?: 0.0,
                        grosirUnit = mBinding.etGrosirUnit.text.toString().toIntOrNull() ?: 0,
                        stok = mBinding.etStock.text.toString().toIntOrNull() ?: 0,
                        status = if (mBinding.tbStatus.isChecked) Product.Status.ACTIVE.value
                        else Product.Status.DISCONTINUED.value,
                        type = mData?.data?.type
                    )
                ))
            } else {
                ToastUtils.showShort("Mohon mengisi semua form!")
            }
        }
    }

    override fun onEditProductLoading() {
        super.onEditProductLoading()
        mLoading.show()
    }

    override fun onEditProductFailed(e: Throwable) {
        mLoading.dismiss()
        ToastUtils.showShort(e.message)
        super.onEditProductFailed(e)
    }

    override fun onEditProductSuccess() {
        mLoading.dismiss()
        ToastUtils.showShort("Berhasil edit produk!")
        setResult(Activity.RESULT_OK)
        finish()
        super.onEditProductSuccess()
    }

    companion object {
        const val ID_KEY = "id"
        fun Context.intentEditProduct(
            id: String
        ) = intentOf<EditFormProductActivity> {
            putExtra(ID_KEY to id)
        }
    }
}
