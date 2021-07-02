/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter

class InvoicesListAdapter(
    data: MutableList<InvoicesListProvider.Type>
) : BaseProviderMultiAdapter<InvoicesListProvider.Type>(data) {

    override fun getItemType(data: List<InvoicesListProvider.Type>, position: Int): Int {
        return when (data[position]) {
            is InvoicesListProvider.Type.Header -> InvoicesListProvider.Layout.Header.value
            is InvoicesListProvider.Type.Invoices -> InvoicesListProvider.Layout.Invoices.value
            is InvoicesListProvider.Type.Total -> InvoicesListProvider.Layout.Total.value
        }
    }

    init {
        addItemProvider(InvoicesListProvider.Header())
        addItemProvider(InvoicesListProvider.Invoices())
        addItemProvider(InvoicesListProvider.Total())
    }
}
