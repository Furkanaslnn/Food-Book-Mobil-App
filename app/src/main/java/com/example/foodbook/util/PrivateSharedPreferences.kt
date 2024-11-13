package com.example.foodbook.util

import android.content.Context
import android.content.SharedPreferences

class PrivateSharedPreferences {

    companion object {

        private val TIME = "time"
        private var SharedPreferences: SharedPreferences? = null

        @Volatile
        private var instance: PrivateSharedPreferences? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createPrivateSharedPreferences(context).also {
                instance = it
            }
        }

        private fun createPrivateSharedPreferences(context: Context): PrivateSharedPreferences {
            SharedPreferences =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return PrivateSharedPreferences()
        }

    }

    fun saveTime(time: Long) {
        SharedPreferences?.edit()?.putLong(TIME, time)?.apply()
    }

    fun getTime() = SharedPreferences?.getLong(TIME, 0)

}