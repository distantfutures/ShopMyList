package com.example.shopmylist.data.repository

import com.example.shopmylist.data.database.ShoppingDatabase
import com.example.shopmylist.data.database.entities.ShoppingItem

class ShoppingRepository(
    private val shoppingDb: ShoppingDatabase
) {
    suspend fun upsert(item: ShoppingItem) = shoppingDb.getShoppingDao().upsert(item)

    suspend fun delete(item: ShoppingItem) = shoppingDb.getShoppingDao().delete(item)

    fun getAllShoppingItems() = shoppingDb.getShoppingDao().getAllShoppingItems()
}