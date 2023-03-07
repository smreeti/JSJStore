package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.jsjstore.adapter.ProductAdapter
import com.android.jsjstore.helper.AppDatabase
import com.android.jsjstore.model.ClientOrder
import com.android.jsjstore.model.Product
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var productDomain: Product
    private var numberOrder = 1
    private var productsRecyclerView: RecyclerView? = null
    private var productsAdapter: ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        var ProductId = intent.getSerializableExtra("ProductId") as String
        bottonNavigation()
        loadProductById(ProductId)
    }

    private fun bottonNavigation() {
        val homeBtn = findViewById<LinearLayout>(R.id.homeBtn)
        val cartBtn = findViewById<LinearLayout>(R.id.cartBtn)
        val checkOut = findViewById<LinearLayout>(R.id.checkoutBtn)

        homeBtn.setOnClickListener {
            startActivity(Intent(this@ProductDetailActivity, HomeActivity::class.java))
        }

        cartBtn.setOnClickListener {
            startActivity(Intent(this@ProductDetailActivity, CartActivity::class.java))
        }
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
                    val txtFee: TextView = findViewById(R.id.txtProductDetailTitle)
                    val txtDescription: TextView = findViewById(R.id.txtProductDetailDescription)
                    val plusBtn: ImageView = findViewById(R.id.plusCardBtn)
                    val minusBtn: ImageView = findViewById(R.id.minusCardBtn)
                    val txtNumberOrdered: TextView = findViewById(R.id.txtNumberItem)
                    val txtTotalPrice: TextView = findViewById(R.id.txtDetailPrice)
                    val txtRanking: TextView = findViewById(R.id.txtDetailRank)
                    val productPicture: ImageView = findViewById(R.id.productDetailImage)
                    val addToCartBtn: TextView = findViewById(R.id.addToCardBtn)

                    txtTotalPrice.text = "Total: $" + (Math.round(numberOrder * product?.price!! * 100).toDouble() / 100).toString()

                    txtTitle.text = product?.title.toString()
                    txtFee.text = product?.price?.toString()
                    txtDescription.text = product?.description
                    txtRanking.text = product?.rank.toString()

                    //can not test this one because on my computer I can not get the images from firebase
                    //please help.
                    Glide.with(productPicture.context).load(product?.picture).into(productPicture)
                    //end help...
                    plusBtn.setOnClickListener {
                        numberOrder += 1
                        txtNumberOrdered.text = numberOrder.toString()
                        txtTotalPrice.text = "Total: $${String.format("%.2f", numberOrder * product?.price!!)}"
                    }

                    minusBtn.setOnClickListener {
                        if (numberOrder > 1) {
                            numberOrder -= 1
                        }
                        txtNumberOrdered.text = numberOrder.toString()
                        txtTotalPrice.text = "Total: $${String.format("%.2f", numberOrder * product?.price!!)}"
                    }

                    addToCartBtn.setOnClickListener {
                        product?.numberInCart = numberOrder.toString()

                        val clientOrder = ClientOrder(
                            productName = product?.title.toString(),
                            quantity = numberOrder,
                            price = product?.price?.toDouble()!!
                        )

                        val database = AppDatabase.getInstance(applicationContext)
                        GlobalScope.launch {
                            database.clientOrderDao().insert(clientOrder)
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
}



