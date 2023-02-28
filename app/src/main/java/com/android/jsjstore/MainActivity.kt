package com.android.jsjstore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<ConstraintLayout>(R.id.btnStart)
        btnStart.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }
    }

}