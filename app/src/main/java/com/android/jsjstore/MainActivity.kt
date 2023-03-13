package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.jsjstore.model.Category
import com.android.jsjstore.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<ConstraintLayout>(R.id.btnStart)
        auth = Firebase.auth

        btnStart.setOnClickListener {
            navigateToHomePage()
        }

        initializeCategories()
        initializeProducts()
    }

    private fun navigateToHomePage() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent);
    }

    private fun initializeCategories() {
        FirebaseDatabase.getInstance().getReference("categories").removeValue()
        dbRef = FirebaseDatabase.getInstance().getReference("categories")

        val categories: List<Category> = listOf(
            Category("Casual", "gs://jsjstore-4e7ce.appspot.com/casual.png"),
            Category("Running", "gs://jsjstore-4e7ce.appspot.com/running.png"),
            Category("Walking", "gs://jsjstore-4e7ce.appspot.com/walking.png"),
            Category("Basketball", "gs://jsjstore-4e7ce.appspot.com/basketball.png"),
            Category("Lifestyle", "gs://jsjstore-4e7ce.appspot.com/lifestyle.png"),
            Category("Football", "gs://jsjstore-4e7ce.appspot.com/football.png")
        )

        for (category in categories) {
            dbRef.child(dbRef.push().key!!).setValue(category)
                .addOnCompleteListener {
                    Log.i("CATEGORY DATA INSERTION", "Categories inserted successfully");
                }
        }
    }

    private fun initializeProducts() {
        FirebaseDatabase.getInstance().getReference("products").removeValue()
        dbRef = FirebaseDatabase.getInstance().getReference("products");
        val products: List<Product> = listOf(
            Product(
                productId = 1,
                "Lacoste Aceshot",
                "gs://jsjstore-4e7ce.appspot.com/7170MTn1XUL._AC_UL1500_-removebg-preview.png",
                "Rubber sole.\n" +
                        "Men's Aceshot Textile Sneakers.\n" +
                        "Textile, synthetic and leather uppers.\n" +
                        "EVA, rubber and synthetic outsole.",
                196.89,
                100,
                "Nike",
                "nike_air_max"
            ),
            Product(
                productId = 2,
                "PUMA Carina",
                "gs://jsjstore-4e7ce.appspot.com/puma.png",
                "Rubber sole.\n" +
                        "Platform.\n" +
                        "Fashion Silhouette.",
                184.73,
                84,
                "Food",
                "nike_jordan"
            ),
            Product(
                productId = 3,
                "Nike Evidence",
                "gs://jsjstore-4e7ce.appspot.com/nike.png",
                "100% Synthetic.\n" +
                        "Rubber sole.\n" +
                        "Premium leather and synthetic.",
                438.36,
                94,
                "Food",
                "air_jordan"
            ),
            Product(
                productId = 4,
                "Timberland Tills",
                "gs://jsjstore-4e7ce.appspot.com/timberland.png",
                "100% Leather.\n" +
                        "Rubber sole.\n" +
                        "Shaft measures approximately Ankle from arch.",
                147.29,
                45,
                "Food",
                "basketball_nike"
            ),
            Product(
                productId = 5,
                "Converse Runstar",
                "gs://jsjstore-4e7ce.appspot.com/converse.png",
                "Mixed Material.\n" +
                        "Rubber sole.\n" +
                        "Black White.",
                298.75,
                69,
                "Food",
                "red_black_nike"
            ),
            Product(
                productId = 6,
                "Vans Hi-Rusty",
                "gs://jsjstore-4e7ce.appspot.com/vanz.png",
                "Features removable felt liners, waterproof rubber lowers.\n" +
                        "Waterproof seam-sealed construction to keep your feet warm and dry.\n" +
                        "Imported.",
                279.45,
                94,
                "Food",
                "nike_blazer"
            ),
            Product(
                productId = 7,
                "Tommy Twlaces",
                "gs://jsjstore-4e7ce.appspot.com/tommy.png",
                "Rubber sole.\n" +
                        "Logo Jogger Sneaker.",
                105.75,
                400,
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