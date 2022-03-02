package com.example.shopmylist.ui

import com.example.shopmylist.data.database.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item: ShoppingItem)
}