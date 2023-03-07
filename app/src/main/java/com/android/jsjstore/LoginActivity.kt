package com.android.jsjstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.jsjstore.databinding.ActivityCartBinding
import com.android.jsjstore.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationMenuBehavior(binding)

    }

    private fun NavigationMenuBehavior(binding : ActivityLoginBinding){
        binding.apply {
            toggle = ActionBarDrawerToggle(this@LoginActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView?.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.cartPage -> {
                        startActivity(Intent(this@LoginActivity, CartActivity::class.java))
                    }
                    R.id.homePage -> {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }
                    R.id.orderPage -> {
                        startActivity(Intent(this@LoginActivity, OrderHistoryActivity::class.java))
                    }
                    R.id.loginPage -> {
                        startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
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