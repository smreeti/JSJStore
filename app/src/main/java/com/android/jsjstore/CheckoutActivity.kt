package com.android.jsjstore

import android.content.Intent
import android.os.AsyncTask
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

            val etAddress = findViewById<EditText>(R.id.etAddress)
            val etCreditCardNumber = findViewById<EditText>(R.id.etCreditCardNumber)
            val etCreditCardName = findViewById<EditText>(R.id.etCreditCartName)
            val etCreditCardExpirationDate = findViewById<EditText>(R.id.etCreditCardExpirationDate)
            val etCvv = findViewById<EditText>(R.id.etCvv)
            val btnCheckout = findViewById<Button>(R.id.btnCheckout)

            btnCheckout.setOnClickListener {
                val address = etAddress.text.toString().trim()
                val creditCardNumber = etCreditCardNumber.text.toString().trim()
                val creditCardName = etCreditCardName.text.toString().trim()
                val expirationDate = etCreditCardExpirationDate.text.toString().trim()
                val cvv = etCvv.text.toString().trim()

                if (address.isEmpty()) {
                    etAddress.error = "Please enter your address"
                    etAddress.requestFocus()
                    return@setOnClickListener
                }

                if (creditCardNumber.isEmpty()) {
                    etCreditCardNumber.error = "Please enter your credit card number"
                    etCreditCardNumber.requestFocus()
                    return@setOnClickListener
                }

                // Validate credit card number
                val regexCreditCardNumber = "^\\d{16}$"
                if (!creditCardNumber.matches(Regex(regexCreditCardNumber))) {
                    etCreditCardNumber.error = "Please enter a valid 16-digit credit card number"
                    etCreditCardNumber.requestFocus()
                    return@setOnClickListener
                }

                if (creditCardName.isEmpty()) {
                    etCreditCardName.error = "Please enter your name on the credit card"
                    etCreditCardName.requestFocus()
                    return@setOnClickListener
                }

                if (expirationDate.isEmpty()) {
                    etCreditCardExpirationDate.error = "Please enter the expiration date"
                    etCreditCardExpirationDate.requestFocus()
                    return@setOnClickListener
                }

                // Validate expiration date
                val regexExpirationDate = "^(0[1-9]|1[0-2])/[0-9]{2,4}$"
                if (!expirationDate.matches(Regex(regexExpirationDate))) {
                    etCreditCardExpirationDate.error = "Please enter a valid expiration date (MM/YY or MM/YYYY)"
                    etCreditCardExpirationDate.requestFocus()
                    return@setOnClickListener
                }

                if (cvv.isEmpty()) {
                    etCvv.error = "Please enter the CVV"
                    etCvv.requestFocus()
                    return@setOnClickListener
                }

                // Validate CVV
                val regexCvv = "^\\d{3}$"
                if (!cvv.matches(Regex(regexCvv))) {
                    etCvv.error = "Please enter a valid 3-digit CVV"
                    etCvv.requestFocus()
                    return@setOnClickListener
                }


                handleCheckoutActivity()
            }

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
                    applicationContext, "Order placed successfully", Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent);
            }
        }

        /**
         * delete room database data using Database Async way
         */
        AsyncTask.execute {
            clientOrderDao.deleteAll()
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
            userInfoMessage?.text = "Dear ${userId}, please provide the following details: "
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