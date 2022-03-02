package com.example.shopmylist.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.example.shopmylist.data.database.entities.ShoppingItem
import kotlinx.android.synthetic.main.dialog_add_shopping_item.*

class AddShoppingItemDialog(context: Context, var addDialogListener: AddDialogListener) : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textViewAdd.setOnClickListener{
            val name = editTextName.text.toString()
            var amount = editTextAmount.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(context, "Please enter the item name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amount.isEmpty()) {
                amount = "1"
            }
            val item = ShoppingItem(name, amount.toInt())
            addDialogListener.onAddButtonClicked(item)
        }
        textViewCancel.setOnClickListener {
            cancel()
        }
    }
}