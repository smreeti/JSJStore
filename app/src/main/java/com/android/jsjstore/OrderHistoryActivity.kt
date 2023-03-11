package com.android.jsjstore

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.adapter.OrderHistoryAdapter
import com.android.jsjstore.databinding.ActivityOrderHistoryBinding
import com.android.jsjstore.model.OrderInfo
import com.android.jsjstore.utils.CommonUtility.Companion.getLoggedInUser
import com.android.jsjstore.utils.CommonUtility.Companion.setNavHeader
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderHistoryBinding
    lateinit var toggle: ActionBarDrawerToggle
    private var orderHistoryAdapter: OrderHistoryAdapter? = null
    private var rView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadOrderHistory()
        navigationMenuBehaviour(binding)
    }

    private fun loadOrderHistory() {
        val loggedInUser = getLoggedInUser(applicationContext)

        if (loggedInUser != "") {
            val databaseReference = FirebaseDatabase.getInstance().reference
            val query = databaseReference.child("orders")
                .child(loggedInUser.replace(".", ","))

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val orderInfoList = mutableListOf<OrderInfo>()

                    for (orderNode in dataSnapshot.children) {
                        val dynamicNodeKey = orderNode.key
                        for (orderDetailNode in orderNode.children) {
                            val order = orderDetailNode.getValue(OrderInfo::class.java)
                            if (order != null) {
                                order.id = dynamicNodeKey.hashCode().toLong()
                                orderInfoList.add(order)
                            }
                        }
                    }
                    // use the orderInfoList to do something with the data
                    orderHistoryAdapter = OrderHistoryAdapter(orderInfoList)
                    rView = findViewById(R.id.orderHistoryRecyclerView)
                    rView?.layoutManager = LinearLayoutManager(this@OrderHistoryActivity)
                    rView?.adapter = orderHistoryAdapter

                    if (orderInfoList.isEmpty()) {
                        handleOrderHistoryMessage("No order(s) yet!")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error fetching orders: ${databaseError.message}")
                }
            })
        } else {
            handleOrderHistoryMessage("Please login to view your orders!")
        }
    }

    private fun handleOrderHistoryMessage(message: String) {
        val txtOrderHistoryMessage: TextView = findViewById(R.id.txtOrderHistoryMessage)
        val orderHistoryScrollView: ScrollView =
            findViewById(R.id.orderHistoryScrollView)

        orderHistoryScrollView.visibility = View.GONE
        txtOrderHistoryMessage.visibility = View.VISIBLE
        txtOrderHistoryMessage.text = message
    }

    private fun navigationMenuBehaviour(binding: ActivityOrderHistoryBinding) {
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@OrderHistoryActivity,
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
                        startActivity(Intent(this@OrderHistoryActivity, CartActivity::class.java))
                    }
                    R.id.homePage -> {
                        startActivity(Intent(this@OrderHistoryActivity, HomeActivity::class.java))
                    }
                    R.id.orderPage -> {
                        startActivity(
                            Intent(
                                this@OrderHistoryActivity,
                                OrderHistoryActivity::class.java
                            )
                        )
                    }
                    R.id.loginOrLogoutPage -> {
                        val loggedInUser = getLoggedInUser(applicationContext)

                        if (loggedInUser == "") {
                            val intent =
                                Intent(this@OrderHistoryActivity, LoginActivity::class.java)
                            intent.putExtra("sidebar", true)
                            startActivity(intent)
                        } else {
                            startActivity(
                                Intent(
                                    this@OrderHistoryActivity,
                                    LogoutActivity::class.java
                                )
                            )
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
}