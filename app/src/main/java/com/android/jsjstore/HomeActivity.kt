package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.MainActivity.Companion.USER_NAME
import com.android.jsjstore.adapter.CategoryAdapter
import com.android.jsjstore.adapter.ProductAdapter
import com.android.jsjstore.databinding.ActivityHomeBinding
import com.android.jsjstore.model.Category
import com.android.jsjstore.model.Product
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {

    private var categoryRecyclerView: RecyclerView? = null
    private var categoryAdapter: CategoryAdapter? = null

    private var productsRecyclerView: RecyclerView? = null
    private var productsAdapter: ProductAdapter? = null

    lateinit var binding : ActivityHomeBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationMenuBehavior(binding)

        //show logged in user name
        //val tvUser: TextView = findViewById(R.id.tvUser)
        //tvUser.text = "Welcome,".plus(intent.getStringExtra(USER_NAME)).plus("!")

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

    private fun NavigationMenuBehavior(binding : ActivityHomeBinding){
        binding.apply {
            toggle = ActionBarDrawerToggle(this@HomeActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.cartPage -> {
                        startActivity(Intent(this@HomeActivity, CartActivity::class.java))
                    }
                    R.id.homePage -> {
                        startActivity(Intent(this@HomeActivity, HomeActivity::class.java))
                    }
                    R.id.orderPage -> {
                        startActivity(Intent(this@HomeActivity, OrderHistoryActivity::class.java))
                    }
                    R.id.loginPage -> {
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    }
                    R.id.checkoutPage -> {
                        startActivity(Intent(this@HomeActivity, CheckoutActivity::class.java))
                    }
                }
                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        categoryAdapter?.startListening()
        productsAdapter?.startListening()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
}