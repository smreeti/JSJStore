package com.android.jsjstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.jsjstore.databinding.ActivityCheckoutBinding
import com.android.jsjstore.databinding.ActivityLoginBinding

class CheckoutActivity : AppCompatActivity() {
    lateinit var binding : ActivityCheckoutBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationMenuBehavior(binding)
    }

    private fun NavigationMenuBehavior(binding : ActivityCheckoutBinding){
        binding.apply {
            toggle = ActionBarDrawerToggle(this@CheckoutActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.cartPage -> {
                        startActivity(Intent(this@CheckoutActivity,
                            CartActivity::class.java))
                    }
                    R.id.homePage -> {
                        startActivity(Intent(this@CheckoutActivity,
                            HomeActivity::class.java))
                    }
                    R.id.orderPage -> {
                        startActivity(Intent(this@CheckoutActivity,
                            OrderHistoryActivity::class.java))
                    }
                    R.id.loginPage -> {
                        startActivity(Intent(this@CheckoutActivity,
                            LoginActivity::class.java))
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