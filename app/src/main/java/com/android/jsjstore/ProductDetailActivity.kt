package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.android.jsjstore.databinding.ActivityProductDetailBinding
import com.android.jsjstore.helper.AppDatabase
import com.android.jsjstore.model.ClientOrder
import com.android.jsjstore.model.Product
import com.android.jsjstore.utils.CommonUtility
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*

class ProductDetailActivity : AppCompatActivity() {
    private var numberOrder = 1
    lateinit var binding: ActivityProductDetailBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationMenuBehaviour(binding)

        var ProductId = intent.getSerializableExtra("ProductId") as String
        loadProductById(ProductId)
    }

    private fun loadProductById(productId: String) {
        val productRef = FirebaseDatabase.getInstance().getReference("products")
            .orderByChild("productId")
            .equalTo(productId.toDouble())

        productRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The product with the specified productId exists
                    val product = dataSnapshot.children.first().getValue(Product::class.java)
                    val txtTitle: TextView = findViewById(R.id.txtProductDetailTitle)
                    val txtDetailPrice: TextView = findViewById(R.id.txtDetailPrice)
                    val txtDescription: TextView = findViewById(R.id.txtProductDetailDescription)
                    val plusBtn: ImageView = findViewById(R.id.plusCardBtn)
                    val minusBtn: ImageView = findViewById(R.id.minusCardBtn)
                    val txtNumberOrdered: TextView = findViewById(R.id.txtNumberItem)
                    val txtTotalPrice: TextView = findViewById(R.id.txtTotalPrice)
                    val txtRanking: TextView = findViewById(R.id.txtDetailRank)
                    val productPicture: ImageView = findViewById(R.id.productDetailImage)
                    val addToCartBtn: TextView = findViewById(R.id.addToCardBtn)

                    txtTotalPrice.text =
                        "Total: $" + (Math.round(numberOrder * product?.price!! * 100)
                            .toDouble() / 100).toString()

                    txtTitle.text = product.title.toString()
                    txtDetailPrice.text = "Unit Price: $" + product.price?.toString()
                    txtDescription.text = product?.description
                    txtRanking.text = product.rank.toString()

                    val storeRef: StorageReference =
                        FirebaseStorage.getInstance().getReferenceFromUrl(product.picture)

                    Glide.with(productPicture.context).load(storeRef).into(productPicture)

                    plusBtn.setOnClickListener {
                        numberOrder += 1
                        txtNumberOrdered.text = numberOrder.toString()
                        txtTotalPrice.text =
                            "Total: $${String.format("%.2f", numberOrder * product?.price!!)}"
                    }

                    minusBtn.setOnClickListener {
                        if (numberOrder > 1) {
                            numberOrder -= 1
                        }
                        txtNumberOrdered.text = numberOrder.toString()
                        txtTotalPrice.text =
                            "Total: $${String.format("%.2f", numberOrder * product?.price!!)}"
                    }

                    addToCartBtn.setOnClickListener {
                        product?.numberInCart = numberOrder.toString()
                        val productName = product?.title.toString()
                        val database = AppDatabase.getInstance(applicationContext)

                        GlobalScope.launch {
                            val existingProduct =
                                database.clientOrderDao().getProductByName(productName)

                            if (existingProduct != null) {
                                // Product exists, update quantity
                                existingProduct.quantity = existingProduct.quantity + numberOrder
                                database.clientOrderDao().update(existingProduct)
                            } else {
                                // Product does not exist, insert new product
                                val clientOrder = ClientOrder(
                                    productName = product?.title.toString(),
                                    quantity = numberOrder,
                                    price = product?.price?.toDouble()!!,
                                    productImage = product?.picture?.toString()!!
                                )
                                database.clientOrderDao().insert(clientOrder)
                            }

                            runOnUiThread {
                                Log.d("ProductDetailActivity", "Showing toast message")
                                Toast.makeText(
                                    this@ProductDetailActivity, "Added to cart",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } else {
                    // The product with the specified productId does not exist
                    Log.e("loadProduct:onCancelled", "Not Found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("loadProduct:onCancelled", "Error")
            }
        })
    }

    private fun navigationMenuBehaviour(binding: ActivityProductDetailBinding) {
        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@ProductDetailActivity,
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
                                this@ProductDetailActivity,
                                CartActivity::class.java
                            )
                        )
                    }
                    R.id.homePage -> {
                        startActivity(
                            Intent(
                                this@ProductDetailActivity,
                                HomeActivity::class.java
                            )
                        )
                    }
                    R.id.orderPage -> {
                        startActivity(
                            Intent(
                                this@ProductDetailActivity,
                                OrderHistoryActivity::class.java
                            )
                        )
                    }
                    R.id.loginOrLogoutPage -> {
                        val loggedInUser = CommonUtility.getLoggedInUser(applicationContext)

                        if (loggedInUser == "") {
                            val intent =
                                Intent(this@ProductDetailActivity, LoginActivity::class.java)
                            intent.putExtra("sidebar", true)
                            startActivity(intent)
                        } else {
                            startActivity(
                                Intent(
                                    this@ProductDetailActivity,
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
        CommonUtility.setNavHeader(applicationContext, navigationView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}



