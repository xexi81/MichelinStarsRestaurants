package com.los3molineros.michelinstarrestaurants.data.sharedPreferences

import android.content.Context
import com.los3molineros.michelinstarrestaurants.common.AppConstants

fun showRateDialogApp(context: Context): Int {
    val prefs = context.getSharedPreferences(AppConstants.PERSONAL_SHARED, 0)

    return prefs.getInt(AppConstants.CONNECTION_TIMES, 0)
}

fun updateNotRateApp(context: Context) {
    val prefs = context.getSharedPreferences(AppConstants.PERSONAL_SHARED, 0)

    prefs.edit().putInt(AppConstants.CONNECTION_TIMES, -1).apply()
}

fun updateRateAppLater(context: Context) {

    val prefs = context.getSharedPreferences(AppConstants.PERSONAL_SHARED, 0)
    val newValue = prefs.getInt(AppConstants.CONNECTION_TIMES, 0) + 1

    if (newValue != 0) {
        prefs.edit().putInt(AppConstants.CONNECTION_TIMES, newValue).apply()
    }

}