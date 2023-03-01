package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.jsjstore.model.Category
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

        initializeDatabase();
    }

    private fun initializeDatabase() {
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
                    Log.i("CATEGORY DATA INSERTION", "Data inserted successfully");
                }
        }
    }
}