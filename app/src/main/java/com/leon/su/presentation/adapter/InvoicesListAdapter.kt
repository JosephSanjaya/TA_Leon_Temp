package com.leon.su.presentation.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter

class InvoicesListAdapter(
    data: MutableList<InvoicesListProvider.Type>
) : BaseProviderMultiAdapter<InvoicesListProvider.Type>(data) {

    override fun getItemType(data: List<InvoicesListProvider.Type>, position: Int): Int {
        return when (data[position]) {
            is InvoicesListProvider.Type.Header -> InvoicesListProvider.Layout.Header.value
            is InvoicesListProvider.Type.InboundHeader -> InvoicesListProvider.Layout.InboundHeader.value
            is InvoicesListProvider.Type.Invoices -> InvoicesListProvider.Layout.Invoices.value
            is InvoicesListProvider.Type.Total -> InvoicesListProvider.Layout.Total.value
            is InvoicesListProvider.Type.Inbound -> InvoicesListProvider.Layout.Inbound.value
        }
    }

    init {
        addItemProvider(InvoicesListProvider.Header())
        addItemProvider(InvoicesListProvider.InboundHeader())
        addItemProvider(InvoicesListProvider.Invoices())
        addItemProvider(InvoicesListProvider.Total())
        addItemProvider(InvoicesListProvider.Inbound())
    }
}
