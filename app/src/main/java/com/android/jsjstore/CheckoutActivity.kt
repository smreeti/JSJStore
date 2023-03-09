package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.android.jsjstore.databinding.ActivityCheckoutBinding
import com.android.jsjstore.helper.AppDatabase
import com.android.jsjstore.model.ClientOrder
import com.android.jsjstore.model.OrderInfo
import com.android.jsjstore.utils.CommonUtility.Companion.getLoggedInUser
import com.android.jsjstore.utils.CommonUtility.Companion.setNavHeader
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckoutBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUserInfoMessage()
        navigationMenuBehaviour(binding)

        val btnCheckout: Button = findViewById(R.id.btnCheckout)
        btnCheckout.setOnClickListener {
            handleCheckoutActivity()
        }
    }

    private fun handleCheckoutActivity() {
        val clientOrderDao = AppDatabase.getInstance(application).clientOrderDao()
        val loggedInUserId = getLoggedInUser(applicationContext)

        //since Firebase database path cannot include ".", so replace it with ","
        dbRef = FirebaseDatabase.getInstance().getReference("orders")
            .child(loggedInUserId.replace(".", ","))

        // Use coroutines to perform database operations on a separate thread
        GlobalScope.launch(Dispatchers.IO) {
            val clientOrder = clientOrderDao.getAll()
            withContext(Dispatchers.Main) {
                val orderInfo: List<OrderInfo> = getOrderInfo(clientOrder, loggedInUserId);
                dbRef.child(dbRef.push().key!!).setValue(orderInfo)
                    .addOnCompleteListener {
                        Log.i("ORDER DATA INSERTION", "Orders inserted successfully");
                    }

                Toast.makeText(
                    applicationContext, "Order places successfully", Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent);
            }
        }
    }

    private fun getOrderInfo(orders: List<ClientOrder>, userId: String): List<OrderInfo> {
        val orderInfoList = mutableListOf<OrderInfo>()
        val etAddress: EditText = findViewById(R.id.etAddress)
        val etCreditCardNumber: EditText = findViewById(R.id.etCreditCardNumber)
        val etCreditCartName: EditText = findViewById(R.id.etCreditCartName)
        val etCreditCardExpirationDate: EditText = findViewById(R.id.etCreditCardExpirationDate)
        val etCvv: EditText = findViewById(R.id.etCvv)

        for (order in orders) {
            val orderInfo = OrderInfo(
                order.id,
                order.productName,
                order.quantity,
                order.price,
                order.productImage,
                userId,
                etAddress.text.toString(),
                etCreditCardNumber.text.toString(),
                etCreditCartName.text.toString(),
                etCreditCardExpirationDate.text.toString(),
                etCvv.text.toString()
            )
            orderInfoList.add(orderInfo)
        }

        return orderInfoList;
    }

    private fun setUserInfoMessage() {
        val userId = getLoggedInUser(applicationContext)
        if (userId != "") {
            val userInfoMessage: TextView = findViewById(R.id.userInfoMessage)
            userInfoMessage.text = "Dear ${userId}, please provide the following details: "
        }
    }

    private fun navigationMenuBehaviour(binding: ActivityCheckoutBinding) {
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@CheckoutActivity,
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
                        startActivity(
                            Intent(
                                this@CheckoutActivity,
                                CartActivity::class.java
                            )
                        )
                    }
                    R.id.homePage -> {
                        startActivity(
                            Intent(
                                this@CheckoutActivity,
                                HomeActivity::class.java
                            )
                        )
                    }
                    R.id.orderPage -> {
                        startActivity(
                            Intent(this@CheckoutActivity, OrderHistoryActivity::class.java)
                        )
                    }
                    R.id.loginOrLogoutPage -> {
                        val loggedInUser = getLoggedInUser(applicationContext)

                        if (loggedInUser == "") {
                            val intent = Intent(this@CheckoutActivity, LoginActivity::class.java)
                            intent.putExtra("sidebar", true)
                            startActivity(intent)
                        } else {
                            startActivity(Intent(this@CheckoutActivity, LogoutActivity::class.java))
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