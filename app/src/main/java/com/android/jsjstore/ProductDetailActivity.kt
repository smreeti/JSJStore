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
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.adapter.ProductAdapter
import com.android.jsjstore.databinding.ActivityProductDetailBinding
import com.android.jsjstore.helper.AppDatabase
import com.android.jsjstore.model.ClientOrder
import com.android.jsjstore.model.Product
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var productDomain: Product
    private var numberOrder = 1
    private var productsRecyclerView: RecyclerView? = null
    private var productsAdapter: ProductAdapter? = null

    lateinit var binding: ActivityProductDetailBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationMenuBehavior(binding)

        var ProductId = intent.getSerializableExtra("ProductId") as String
        loadProductById(ProductId)
    }

    private fun loadProductById(productId: String) {

        Log.e("loadProduct:onCancelled", productId.toString())
        val productRef = FirebaseDatabase.getInstance().getReference("products")
            .orderByChild("productId")
            .equalTo(productId.toDouble())

        productRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The product with the specified productId exists
                    val product = dataSnapshot.children.first().getValue(Product::class.java)
                    val txtTitle: TextView = findViewById(R.id.txtProductDetailTitle)
                    val txtFee: TextView = findViewById(R.id.txtDetailPrice)
                    val txtDescription: TextView = findViewById(R.id.txtProductDetailDescription)
                    val plusBtn: ImageView = findViewById(R.id.plusCardBtn)
                    val minusBtn: ImageView = findViewById(R.id.minusCardBtn)
                    val txtNumberOrdered: TextView = findViewById(R.id.txtNumberItem)
                    val txtTotalPrice: TextView = findViewById(R.id.txtDetailPrice)
                    val txtRanking: TextView = findViewById(R.id.txtDetailRank)
                    val productPicture: ImageView = findViewById(R.id.productDetailImage)
                    val addToCartBtn: TextView = findViewById(R.id.addToCardBtn)

                    txtTotalPrice.text =
                        "Total: $" + (Math.round(numberOrder * product?.price!! * 100)
                            .toDouble() / 100).toString()

                    txtTitle.text = product?.title.toString()
                    txtFee.text = product?.price?.toString()
                    txtDescription.text = product?.description
                    txtRanking.text = product?.rank.toString()

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
                        val clientOrder = ClientOrder(
                            productName = product?.title.toString(),
                            quantity = numberOrder,
                            price = product?.price?.toDouble()!!,
                            productImage = product?.picture?.toString()!!
                        )
                        val database = AppDatabase.getInstance(applicationContext)
                        GlobalScope.launch {
                            database.clientOrderDao().insert(clientOrder)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    applicationContext, "Added to cart",
                                    Toast.LENGTH_SHORT
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

    private fun NavigationMenuBehavior(binding: ActivityProductDetailBinding) {
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
                    R.id.loginPage -> {
                        startActivity(
                            Intent(
                                this@ProductDetailActivity,
                                LoginActivity::class.java
                            )
                        )
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



