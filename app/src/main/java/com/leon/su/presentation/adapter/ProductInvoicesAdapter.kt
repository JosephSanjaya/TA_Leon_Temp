/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.adapter

import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.leon.su.R
import com.leon.su.databinding.RowEmptyListBinding
import com.leon.su.databinding.RowProductBinding
import com.leon.su.domain.Product
import com.leon.su.utils.toRupiah

class ProductInvoicesAdapter(
    layoutInflater: LayoutInflater,
    data: MutableList<Product.Cart>
) :
    BaseQuickAdapter<Product.Cart, BaseDataBindingHolder<RowProductBinding>>(
        R.layout.row_product,
        data
    ) {

    var fullData: MutableList<Product.Cart> = ArrayList()

    init {
        setEmptyView(RowEmptyListBinding.inflate(layoutInflater).root)
        fullData = data
        animationEnable = true
    }

    fun updateData(data: List<Product.Response>) {
        val added = data.map {
            Product.Cart(
                product = it.product
            )
        }.toMutableList()
        fullData = added
        setNewInstance(added)
    }

    fun reset() = setNewInstance(fullData)
    fun filter(search: String) = setNewInstance(
        fullData.filter {
            it.product?.namaProduct?.contains(search, ignoreCase = true) == true
        }.toMutableList()
    )

    override fun convert(
        holder: BaseDataBindingHolder<RowProductBinding>,
        item: Product.Cart
    ) {
        holder.dataBinding?.apply {
            val stok = "Stok: ${item.product?.stok}"
            tvNamaProduct.text = item.product?.namaProduct
            tvStock.text = stok
            val hargaEcer = "${item.product?.hargaEcer?.toRupiah()} / pcs"
            val hargaGrosir = "${item.product?.hargaGrosir?.toRupiah()} / dus"
            tvHargaEcer.text = hargaEcer
            tvHargaGrosir.text = hargaGrosir
        }
    }
}
