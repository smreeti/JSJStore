package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.adapter.ClientOrderAdapter
import com.android.jsjstore.databinding.ActivityCartBinding
import com.android.jsjstore.helper.AppDatabase
import com.android.jsjstore.model.ClientOrder
import com.android.jsjstore.utils.CommonUtility
import com.android.jsjstore.utils.CommonUtility.Companion.setNavHeader
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private var cartRecyclerView: RecyclerView? = null
    private lateinit var adapter: ClientOrderAdapter
    private var tax = 0.0
    lateinit var binding: ActivityCartBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationMenuBehavior(binding)

        cartRecyclerView = findViewById(R.id.CartRecicleView)
        cartRecyclerView?.layoutManager = LinearLayoutManager(this)

        val dao = AppDatabase.getInstance(application).clientOrderDao()

        // Use coroutines to perform database operations on a separate thread
        GlobalScope.launch(Dispatchers.IO) {
            val orders = dao.getAll()

            // Update the UI on the main thread
            withContext(Dispatchers.Main) {
                adapter = ClientOrderAdapter(orders)
                cartRecyclerView?.adapter = adapter
            }
            calculateCard(orders)
        }
        handleCheckoutActivity();
    }

    private fun handleCheckoutActivity() {
        val tvCheckout: (TextView) = findViewById(R.id.tvCheckout)
        tvCheckout.setOnClickListener {
            val userId = CommonUtility.getLoggedInUser(applicationContext)

            if (userId != "") {
                val intent = Intent(applicationContext, CheckoutActivity::class.java)
                startActivity(intent);
            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent);
            }
        }
    }

    private fun calculateCard(orders: List<ClientOrder>) {
        val txtTotal: TextView = findViewById(R.id.txtTotal)
        val txtDeliveryServices: TextView = findViewById(R.id.txtDeliveryServices)
        val txtTax: TextView = findViewById(R.id.txtTax)
        val txtItemTotal: TextView = findViewById(R.id.txtItemTotal)
        val txtEmptyCard: TextView = findViewById(R.id.txtEmptyCard)
        val scrollView: ScrollView = findViewById(R.id.cardScrollView)

        if (orders.isNotEmpty()) {
            txtEmptyCard.visibility = View.GONE
            scrollView.visibility = View.VISIBLE

            val totalFee = orders.sumOf { it.price }
            val percentageTax = 0.02 // change this for tax price
            val delivery = 10.00 // change this for delivery prices
            tax = Math.round(totalFee * percentageTax * 100.0) / 100.0
            val total = Math.round((totalFee + tax + delivery) * 100.0) / 100.00
            val itemTotal = Math.round(totalFee * 100.00) / 100.00

            txtTotal.setText("$$total")
            txtTax.setText("$$tax")
            txtDeliveryServices.setText("$$delivery")
            txtItemTotal.setText("$$itemTotal")
        } else {
            txtEmptyCard.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        }
    }

    private fun navigationMenuBehavior(binding: ActivityCartBinding) {
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@CartActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.cartPage -> {
                        startActivity(Intent(this@CartActivity, CartActivity::class.java))
                    }
                    R.id.homePage -> {
                        startActivity(Intent(this@CartActivity, HomeActivity::class.java))
                    }
                    R.id.orderPage -> {
                        startActivity(Intent(this@CartActivity, OrderHistoryActivity::class.java))
                    }
                    R.id.loginOrLogoutPage -> {
                        val loggedInUser = CommonUtility.getLoggedInUser(applicationContext)

                        if (loggedInUser == "") {
                            val intent = Intent(this@CartActivity, LoginActivity::class.java)
                            intent.putExtra("sidebar", true)
                            startActivity(intent)
                        } else {
                            startActivity(Intent(this@CartActivity, LogoutActivity::class.java))
                        }
                    }
                }
                true
            }
        }
        // Get a reference to the NavigationView
        val navigationView = findViewById<NavigationView>(R.id.navView)
        setNavHeader(applicationContext, navigationView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        // Return true so that the menu gets displayed.
        return true
    }

}