package com.android.jsjstore

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.MainActivity.Companion.USER_NAME
import com.android.jsjstore.adapter.CategoryAdapter
import com.android.jsjstore.adapter.ProductAdapter
import com.android.jsjstore.model.Category
import com.android.jsjstore.model.Product
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {

    private var categoryRecyclerView: RecyclerView? = null
    private var categoryAdapter: CategoryAdapter? = null

    private var productsRecyclerView: RecyclerView? = null
    private var productsAdapter: ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //show logged in user name
        val tvUser: TextView = findViewById(R.id.tvUser)
        tvUser.text = "Welcome,".plus(intent.getStringExtra(USER_NAME)).plus("!")

        loadCategoryRecyclerView()
        loadProductsRecyclerView()
    }

    private fun loadCategoryRecyclerView() {
        val query = FirebaseDatabase.getInstance().reference.child("categories")
        val options = FirebaseRecyclerOptions.Builder<Category>()
            .setQuery(query, Category::class.java)
            .build()

        categoryAdapter = CategoryAdapter(options)
        categoryRecyclerView = findViewById(R.id.categoriesView)
        categoryRecyclerView?.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        categoryRecyclerView?.adapter = categoryAdapter
    }

    private fun loadProductsRecyclerView() {
        val query = FirebaseDatabase.getInstance().reference.child("products")
        val options = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        productsAdapter = ProductAdapter(options)
        productsRecyclerView = findViewById(R.id.recommendedView)
        productsRecyclerView?.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        productsRecyclerView?.adapter = productsAdapter
    }

    override fun onStart() {
        super.onStart()
        categoryAdapter?.startListening()
        productsAdapter?.startListening()
    }
}