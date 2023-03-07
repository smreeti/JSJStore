package com.android.jsjstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.jsjstore.databinding.ActivityLoginBinding
import com.android.jsjstore.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : AppCompatActivity() {
    lateinit var binding : ActivityOrderHistoryBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationMenuBehavior(binding)

    }

    private fun NavigationMenuBehavior(binding : ActivityOrderHistoryBinding){
        binding.apply {
            toggle = ActionBarDrawerToggle(this@OrderHistoryActivity, drawerLayout, R.string.open, R.string.close)
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
                        startActivity(Intent(this@OrderHistoryActivity, OrderHistoryActivity::class.java))
                    }
                    R.id.loginPage -> {
                        startActivity(Intent(this@OrderHistoryActivity, LoginActivity::class.java))
                    }
                }
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
}