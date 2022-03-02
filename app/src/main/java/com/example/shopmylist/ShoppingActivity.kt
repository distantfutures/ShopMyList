package com.example.shopmylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopmylist.adapters.ShoppingItemAdapter
import com.example.shopmylist.data.database.ShoppingDatabase
import com.example.shopmylist.data.database.entities.ShoppingItem
import com.example.shopmylist.data.repository.ShoppingRepository
import com.example.shopmylist.ui.AddDialogListener
import com.example.shopmylist.ui.AddShoppingItemDialog
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModel
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

// (4) Kodein
class ShoppingActivity : AppCompatActivity(), KodeinAware {
    // (5) Kodein
    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // (6) Kodein - delete below and add in ShoppingApplication to Manifest
//        // Bad practice bc instantiating all of these in this activity, makes it dependent on this activity if there are any changes to this activity
//        // Better to have a global place to instantiate objects and pass them from there (Dependency Injection)
//        val database = ShoppingDatabase(this)
//        val repository = ShoppingRepository(database)
//        val factory = ShoppingViewModelFactory(repository)

        val viewModel = ViewModelProvider(this, factory).get(ShoppingViewModel::class.java)

        val adapter = ShoppingItemAdapter(listOf(), viewModel)

        recyclerViewShoppingItems.layoutManager = LinearLayoutManager(this)
        recyclerViewShoppingItems.adapter = adapter

        viewModel.getAllShoppingItem().observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            AddShoppingItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    viewModel.upsert(item)
                }
            }).show()
        }
    }
}