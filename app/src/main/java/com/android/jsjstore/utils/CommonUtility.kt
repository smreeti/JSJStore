package com.android.jsjstore.utils

import android.content.Context
import android.content.SharedPreferences

class CommonUtility {
    companion object {
        fun getLoggedInUser(applicationContext: Context): String {
            val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
                "userInfo",
                Context.MODE_PRIVATE
            )
            return sharedPreferences.getString("userId", "")!!
        }
    }
}