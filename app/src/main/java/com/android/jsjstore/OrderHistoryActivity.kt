package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.adapter.OrderHistoryAdapter
import com.android.jsjstore.databinding.ActivityOrderHistoryBinding
import com.android.jsjstore.model.OrderInfo
import com.android.jsjstore.utils.CommonUtility
import com.android.jsjstore.utils.CommonUtility.Companion.getLoggedInUser
import com.google.firebase.database.FirebaseDatabase

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
            val query = FirebaseDatabase.getInstance().getReference("orders")
                .child(loggedInUser.replace(".", ","))

            query.get().addOnSuccessListener { orderInfo ->
                run {
                    val orderInfoList = orderInfo.children

                    orderInfoList.forEach { orders ->
                        run {
                            val order = orders.getValue(OrderInfo::class.java)
                            orderHistoryAdapter = order?.let { OrderHistoryAdapter(it) }
                        }

                    }
                }
            }


//            val options =
//                FirebaseRecyclerOptions.Builder<OrderInfo>()
//                    .setQuery(query, OrderInfo::class.java)
//                    .build()
//            orderHistoryAdapter = OrderHistoryAdapter(options)

            rView = findViewById(R.id.orderHistoryRecyclerView)
            rView?.layoutManager = LinearLayoutManager(this)
            rView?.adapter = orderHistoryAdapter
        }
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
                        val loggedInUser = CommonUtility.getLoggedInUser(applicationContext)

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}