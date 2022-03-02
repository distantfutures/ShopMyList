package com.example.shopmylist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmylist.R
import com.example.shopmylist.data.database.entities.ShoppingItem
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModel
import kotlinx.android.synthetic.main.shopping_item.view.*

class ShoppingItemAdapter(
    var items: List<ShoppingItem>,
    private val viewModel: ShoppingViewModel
): RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {

    inner class ShoppingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemView.textViewName.text = currentItem.name
        holder.itemView.textViewAmount.text = currentItem.amount.toString()

        holder.itemView.imageViewDelete.setOnClickListener {
            viewModel.delete(currentItem)
        }
        holder.itemView.imageViewPlus.setOnClickListener {
            currentItem.amount++
            viewModel.upsert(currentItem)
        }
        holder.itemView.imageViewMinus.setOnClickListener {
            if (currentItem.amount > 0) {
                currentItem.amount--
                viewModel.upsert(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}