package com.example.shopmylist.ui.shoppinglist

import androidx.lifecycle.ViewModel
import com.example.shopmylist.data.database.entities.ShoppingItem
import com.example.shopmylist.data.repository.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository
):ViewModel() {
    // in a nutshell...A channel is something you can send object into and receive object out of
    private val itemEventChannel = Channel<ItemEvent>()
    val eventFlow = itemEventChannel.receiveAsFlow()

    fun upsert(item: ShoppingItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(item)
    }
    fun delete(item: ShoppingItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
        // Passes the item that was swiped, through the Channel
        itemEventChannel.send(ItemEvent.ShowUndoDeleteItemMessage(item))
    }
    fun getAllShoppingItem() = repository.getAllShoppingItems()

    fun onUndoDelete(item: ShoppingItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(item)
    }
    // DIGEST!!
    sealed class ItemEvent {
        data class ShowUndoDeleteItemMessage(val item: ShoppingItem) : ItemEvent()
    }
}