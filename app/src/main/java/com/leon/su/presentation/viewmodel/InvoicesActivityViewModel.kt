package com.leon.su.presentation.viewmodel

import com.leon.su.domain.Product

class InvoicesActivityViewModel : BaseViewModel() {

    val mCartItem = mutableListOf<Product.Cart>()
    fun setNewQuantity(data: Product.Cart) {
        val index = mCartItem.indexOfFirst {
            it.product?.id == data.product?.id
        }
        if (index != -1) {
            if (data.quantity == 0) {
                mCartItem.removeAt(index)
            } else {
                mCartItem[index] = data
            }
        } else {
            if (data.quantity != 0) {
                mCartItem.add(data)
            }
        }
    }
}
