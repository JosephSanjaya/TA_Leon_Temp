package com.leon.su.presentation.adapter

import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.google.firebase.storage.StorageReference
import com.leon.su.R
import com.leon.su.databinding.RowEmptyListBinding
import com.leon.su.databinding.RowHistoryBinding

class HistoryListAdapter(
    layoutInflater: LayoutInflater,
    data: MutableList<StorageReference>
) :
    BaseQuickAdapter<StorageReference, BaseDataBindingHolder<RowHistoryBinding>>(
        R.layout.row_history,
        data
    ) {

    var fullData: MutableList<StorageReference> = ArrayList()

    init {
        setEmptyView(RowEmptyListBinding.inflate(layoutInflater).root)
        addChildClickViewIds(R.id.clRoot)
        fullData = data
        animationEnable = true
    }

    fun updateData(data: List<StorageReference>) {
        val added = data.sortedBy {
            it.name
        }.toMutableList()
        fullData = added
        setNewInstance(added)
    }

    fun reset() = setNewInstance(fullData)
    fun filter(search: String) = setNewInstance(
        fullData.filter {
            it.name.contains(search, ignoreCase = true)
        }.sortedBy {
            it.name
        }.toMutableList()
    )

    override fun convert(
        holder: BaseDataBindingHolder<RowHistoryBinding>,
        item: StorageReference
    ) {
        holder.dataBinding?.apply {
            rowContent.text = item.name
        }
    }
}
