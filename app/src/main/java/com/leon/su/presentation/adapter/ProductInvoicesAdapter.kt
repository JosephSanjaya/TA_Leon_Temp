package com.leon.su.presentation.adapter

import android.view.LayoutInflater
import androidx.core.view.isGone
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.leon.su.R
import com.leon.su.databinding.RowEmptyListBinding
import com.leon.su.databinding.RowProductInvoicesBinding
import com.leon.su.domain.Product
import com.leon.su.utils.toRupiah

class ProductInvoicesAdapter(
    layoutInflater: LayoutInflater,
    data: MutableList<Product.Cart>
) :
    BaseQuickAdapter<Product.Cart, BaseDataBindingHolder<RowProductInvoicesBinding>>(
        R.layout.row_product_invoices,
        data
    ) {

    var fullData: MutableList<Product.Cart> = ArrayList()

    init {
        setEmptyView(RowEmptyListBinding.inflate(layoutInflater).root)
        fullData = data
        animationEnable = false
        addChildClickViewIds(
            R.id.tvMinusEcer,
            R.id.tvMinusGrosir,
            R.id.tvPlusEcer,
            R.id.tvPlusGrosir
        )
    }

    fun updateData(data: List<Product.Response>, current: MutableList<Product.Cart>) {
        val added = data.map { added ->
            Product.Cart(
                product = added,
                quantity = current.firstOrNull {
                    it.product?.id == added.id
                }?.quantity ?: 0
            )
        }.sortedBy {
            it.product?.data?.namaProduct
        }.toMutableList().toMutableList()
        fullData = added
        setNewInstance(added)
    }

    fun reset(current: MutableList<Product.Cart>) = updateData(
        fullData.filter {
            it.product != null
        }.map {
            it.product!!
        },
        current
    )

    fun filter(search: String, current: MutableList<Product.Cart>) = setNewInstance(
        fullData.filter {
            it.product?.data?.namaProduct?.contains(search, ignoreCase = true) == true
        }.map { filtered ->
            filtered.apply {
                quantity = current.firstOrNull {
                    filtered.product?.id == it.product?.id
                }?.quantity ?: 0
            }
        }.sortedBy {
            it.product?.data?.namaProduct
        }.toMutableList()
    )

    override fun convert(
        holder: BaseDataBindingHolder<RowProductInvoicesBinding>,
        item: Product.Cart
    ) {
        holder.dataBinding?.apply {
            val stok = "Stok: ${item.product?.data?.stok}"
            tvNamaProduct.text = item.product?.data?.namaProduct
            tvStock.text = stok
            cvEcer.isGone = item.product?.data?.hargaEcer == 0.0
            cvGrosir.isGone = item.product?.data?.hargaGrosir == 0.0
            val hargaEcer = "${item.product?.data?.hargaEcer?.toRupiah()} / pcs"
            val hargaGrosir = "${item.product?.data?.hargaGrosir?.toRupiah()} / dus"
            val perDus = if (item.product?.data?.grosirUnit ?: 0 <= 1) {
                "(Satuan)"
            } else {
                "(${item.product?.data?.grosirUnit ?: 0} pcs per dus)"
            }
            tvPerDus.text = perDus
            tvHargaEcer.text = hargaEcer
            tvHargaGrosir.text = hargaGrosir
            if (item.product?.data?.grosirUnit ?: 0 > 1 &&
                item.quantity >= item.product?.data?.grosirUnit ?: 0
            ) {
                val ecerGrosir = item.getEcerGrosir()
                tvQtyEcer.text = ecerGrosir.first.toString()
                tvQtyGrosir.text = ecerGrosir.second.toString()
            } else {
                tvQtyEcer.text = item.quantity.toString()
                tvQtyGrosir.text = 0.toString()
            }
        }
    }
}
