package com.leon.su.presentation.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.leon.su.R
import com.leon.su.databinding.*
import com.leon.su.domain.Product
import com.leon.su.utils.toRupiah
import com.soywiz.klock.DateTime

object InvoicesListProvider {
    enum class Layout(val value: Int) {
        Header(0),
        InboundHeader(1),
        Invoices(2),
        Inbound(3),
        Total(4),
    }

    sealed class Type {
        class Header(val date: DateTime, val nama: String) : Type()
        class InboundHeader(val date: DateTime, val nama: String) : Type()
        class Invoices(val data: Product.Cart) : Type()
        class Inbound(val data: Product.Cart) : Type()
        class Total(val total: Double) : Type()
    }

    class Header(
        override val itemViewType: Int = Layout.Header.value,
        override val layoutId: Int = R.layout.row_invoices_header
    ) : BaseItemProvider<Type>() {
        override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
            DataBindingUtil.bind<RowInvoicesHeaderBinding>(viewHolder.itemView)
            super.onViewHolderCreated(viewHolder, viewType)
        }

        override fun convert(helper: BaseViewHolder, item: Type) {
            item as Type.Header
            helper.getBinding<RowInvoicesHeaderBinding>()?.apply {
                tvUser.text = item.nama
                tvTanggal.text = item.date.format("dd MMMM yyyy HH:mm:ss")
            }
        }
    }

    class InboundHeader(
        override val itemViewType: Int = Layout.InboundHeader.value,
        override val layoutId: Int = R.layout.row_inbound_header
    ) : BaseItemProvider<Type>() {
        override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
            DataBindingUtil.bind<RowInboundHeaderBinding>(viewHolder.itemView)
            super.onViewHolderCreated(viewHolder, viewType)
        }

        override fun convert(helper: BaseViewHolder, item: Type) {
            item as Type.InboundHeader
            helper.getBinding<RowInboundHeaderBinding>()?.apply {
                tvUser.text = item.nama
                tvTanggal.text = item.date.format("dd MMMM yyyy HH:mm:ss")
            }
        }
    }

    class Invoices(
        override val itemViewType: Int = Layout.Invoices.value,
        override val layoutId: Int = R.layout.row_invoices
    ) : BaseItemProvider<Type>() {

        override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
            DataBindingUtil.bind<RowInvoicesBinding>(viewHolder.itemView)
            super.onViewHolderCreated(viewHolder, viewType)
        }

        override fun convert(helper: BaseViewHolder, item: Type) {
            item as Type.Invoices
            helper.getBinding<RowInvoicesBinding>()?.apply {
                tvNamaProduct.text = item.data.product?.data?.namaProduct
                val ecerGrosir = item.data.getEcerGrosir()
                val quantity = when {
                    ecerGrosir.second == 0 -> "${ecerGrosir.first} pcs"
                    ecerGrosir.first == 0 -> "${ecerGrosir.second} dus"
                    else -> "${ecerGrosir.second} dus, ${ecerGrosir.first} pcs"
                }
                tvQuantity.text = quantity
                val total = item.data.getTotal().toRupiah()
                tvTotal.text = total
            }
        }
    }

    class Inbound(
        override val itemViewType: Int = Layout.Inbound.value,
        override val layoutId: Int = R.layout.row_inbound
    ) : BaseItemProvider<Type>() {

        override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
            DataBindingUtil.bind<RowInboundBinding>(viewHolder.itemView)
            super.onViewHolderCreated(viewHolder, viewType)
        }

        override fun convert(helper: BaseViewHolder, item: Type) {
            item as Type.Inbound
            helper.getBinding<RowInboundBinding>()?.apply {
                tvNamaProduct.text = item.data.product?.data?.namaProduct
                val ecerGrosir = item.data.getEcerGrosir()
                val quantity = when {
                    ecerGrosir.second == 0 -> "${ecerGrosir.first} pcs"
                    ecerGrosir.first == 0 -> "${ecerGrosir.second} dus"
                    else -> "${ecerGrosir.second} dus, ${ecerGrosir.first} pcs"
                }
                tvQuantity.text = quantity
            }
        }
    }

    class Total(
        override val itemViewType: Int = Layout.Total.value,
        override val layoutId: Int = R.layout.row_invoices_total
    ) : BaseItemProvider<Type>() {

        override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
            DataBindingUtil.bind<RowInvoicesTotalBinding>(viewHolder.itemView)
            super.onViewHolderCreated(viewHolder, viewType)
        }

        override fun convert(helper: BaseViewHolder, item: Type) {
            item as Type.Total
            helper.getBinding<RowInvoicesTotalBinding>()?.apply {
                tvTotal.text = item.total.toRupiah()
            }
        }
    }
}
