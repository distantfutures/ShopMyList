package com.example.shopmylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.shopmylist.data.database.ShoppingDatabase
import com.example.shopmylist.data.repository.ShoppingRepository
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModel
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModelFactory

class ShoppingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bad practice bc instantiating all of these in this activity, makes it dependent on this activity if there are any changes to this activity
        // Better to have a global place to instantiate objects and pass them from there (Dependency Injection)
        val database = ShoppingDatabase(this)
        val repository = ShoppingRepository(database)
        val factory = ShoppingViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory).get(ShoppingViewModel::class.java)
    }
}