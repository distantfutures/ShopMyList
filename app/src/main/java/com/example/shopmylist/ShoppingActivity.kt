package com.example.shopmylist

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopmylist.adapters.ShoppingItemAdapter
import com.example.shopmylist.data.database.entities.ShoppingItem
import com.example.shopmylist.ui.AddDialogListener
import com.example.shopmylist.ui.AddShoppingItemDialog
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModel
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

// (4) Kodein
class ShoppingActivity : AppCompatActivity(), KodeinAware {
    // (5) Kodein
    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.P)
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

        // a Flow can only be collected from a Coroutine because it can call suspend when waiting for new values
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                // when ItemEvent is passed though the eventFlow, it shows a Snackbar
                when (event) {
                    is ShoppingViewModel.ItemEvent.ShowUndoDeleteItemMessage -> {
                        Snackbar.make(requireViewById(android.R.id.content), "Item Deleted!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDelete(event.item)
                            }.show()
                    }
                }
            }
        }

        fab.setOnClickListener {
            AddShoppingItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    viewModel.upsert(item)
                }
            }).show()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.items[viewHolder.adapterPosition]
                viewModel.delete(item)
            }
        }).attachToRecyclerView(recyclerViewShoppingItems)
    }
}