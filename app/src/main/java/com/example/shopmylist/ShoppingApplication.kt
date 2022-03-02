package com.example.shopmylist

import android.app.Application
import com.example.shopmylist.data.database.ShoppingDatabase
import com.example.shopmylist.data.repository.ShoppingRepository
import com.example.shopmylist.ui.shoppinglist.ShoppingViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

// (1) Kodein
class ShoppingApplication: Application(), KodeinAware {
    // (2) Kodein
    // lazy implementation means inside the block we can use the Application context during binding time
    override val kodein: Kodein = Kodein.lazy{
        // (3) Kodein
        import(androidXModule(this@ShoppingApplication))
        bind() from singleton { ShoppingDatabase(instance()) }
        bind() from singleton { ShoppingRepository(instance()) }
        bind() from provider { ShoppingViewModelFactory(instance()) }
    }
}