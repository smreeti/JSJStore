package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.jsjstore.model.Category
import com.android.jsjstore.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<ConstraintLayout>(R.id.btnStart)
        btnStart.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }

        initializeCategories()
        initializeProducts()
    }

    private fun initializeCategories() {
        dbRef = FirebaseDatabase.getInstance().getReference("categories");
        val categories: ArrayList<Category> = ArrayList()

        categories.add(Category("Casual", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"))
        categories.add(Category("Running", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"))
        categories.add(Category("Walking", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"))
        categories.add(Category("Basketball", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"))
        categories.add(Category("Lifestyle", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"))
        categories.add(Category("Football", "gs://jsjstore-4e7ce.appspot.com/running_cat.png"))

        for (category in categories) {
            dbRef.child(dbRef.push().key!!).setValue(category)
                .addOnCompleteListener {
                    Log.i("CATEGORY DATA INSERTION", "Categories inserted successfully");
                }
        }
    }

    private fun initializeProducts(){
        dbRef = FirebaseDatabase.getInstance().getReference("products");
        val products: ArrayList<Product> = ArrayList()

        products.add(Product(
                "Nike Air Max",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "helps keeps your legs fresh late into the fourth quarter for better individual performances and team wins. Sneakers such as the Nike Air Zoom G.T. Run stacks a React footbed on top of a React midsole and Zoom Air in the forefoot to provide shock absorption and a responsive ride.",
                130.99,
                5,
                "Nike",
                "nike_air_max"
            )
        )

        products.add(
            Product(
                "Nike Jordan",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "The Jordan' Why Not?' line proves that speed is the most important factor on the court. With two stacked Zoom Air units in the forefoot for responsive cushioning, they help you harness more of your own speed to change the game.",
                179.99,
                4,
                "Food",
                "nike_jordan"
            )
        )

        products.add(
            Product(
                "Air Jordan Nike",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "This shoes are all about speed and power. They have a full-length Zoom Air unit and Max Air in the heel to absorb impact and return energy. And the KnitPosite 2.0 upper provides a secure fit to help you move with confidence.",
                528.99,
                4,
                "Food",
                "air_jordan"
            )
        )

        products.add(
            Product(
                "Basketball Nike",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "Nike KD basketball shoes have a durable upper that uses minimal material for a broken-in feel. Full-length Zoom Air cushioning",
                310.99,
                4,
                "Food",
                "basketball_nike"
            )
        )

        products.add(
            Product(
                "Nike AIR Jordan 6 Retro",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "Designed by the legendary Tinker Hatfield, the sixth Air Jordan edition had everything fans loved previous models for.",
                799.99,
                4,
                "Food",
                "red_black_nike"
            )
        )

        products.add(
            Product(
                "Nike Blazers",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "Nike Women's W Blazer Low LX Red Stardust Sneakers, Rubber sole, Leather upper for durability and premium comfort, Autoclave construction fuses the outsole to the midsole for a streamlined look, Rubber outsole in a herringbone pattern for traction and durability.",
                499.99,
                4,
                "Food",
                "nike_blazer"
            )
        )

        products.add(
            Product(
                "Nike Classic",
                "gs://jsjstore-4e7ce.appspot.com/running_cat.png",
                "The Nike Classic Cortez Leather is the original Nike running shoe perfected by an innovative, ultra-soft cushioning system. The shoe is made with a premium leather upper for comfort and durability and with the EVA wedge midsole for lightweight cushioning. Rubber sole provides unbeatable traction and resistance.",
                99.99,
                4,
                "Food",
                "nike_classic"
            )
        )

        for (product in products) {
            dbRef.child(dbRef.push().key!!).setValue(product)
                .addOnCompleteListener {
                    Log.i("PRODUCTS DATA INSERTION ::::::", "Products inserted successfully");
                }
        }
    }
}