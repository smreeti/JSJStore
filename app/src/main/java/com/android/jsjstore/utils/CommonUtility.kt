package com.android.jsjstore.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.jsjstore.R
import com.google.android.material.navigation.NavigationView

class CommonUtility : AppCompatActivity() {
    companion object {
        fun getLoggedInUser(applicationContext: Context): String {
            val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
                "userInfo",
                Context.MODE_PRIVATE
            )
            return sharedPreferences.getString("userId", "")!!
        }

        private fun getLoggedInDisplayName(applicationContext: Context): String {
            val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
                "userInfo",
                Context.MODE_PRIVATE
            )
            return sharedPreferences.getString("displayName", "")!!
        }

        fun clearSharedPreference(applicationContext: Context) {
            val settings: SharedPreferences = applicationContext.getSharedPreferences(
                "userInfo", MODE_PRIVATE
            )
            settings.edit().clear().apply()
        }

        fun setNavHeader(applicationContext: Context, navigationView: NavigationView) {
            val loggedInUser = getLoggedInUser(applicationContext)
            val loggedInDisplayName = getLoggedInDisplayName(applicationContext)
            // Get a reference to the header view
            val headerView = navigationView.getHeaderView(0)

            if (loggedInUser != "") {
                // Find the TextView within the header view
                val tvDisplayName = headerView.findViewById<TextView>(R.id.tvDisplayName)
                val tvEmail = headerView.findViewById<TextView>(R.id.tvEmail)

                tvDisplayName.text = loggedInDisplayName
                tvEmail.text = loggedInUser

                //if the user is already logged in, then change the title as 'Log out' else vice versa
                val menu = navigationView.menu
                val loginOrLogoutPage = menu.findItem(R.id.loginOrLogoutPage)
                loginOrLogoutPage.title = "Logout"
            }
        }
    }
}