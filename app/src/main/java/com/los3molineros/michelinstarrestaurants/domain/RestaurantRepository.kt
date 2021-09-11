package com.los3molineros.michelinstarrestaurants.domain

import android.content.Context
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>

    fun showDialogRateApp(context: Context): Boolean
    fun dontShowDialogNeverMore(context: Context)
    fun showDialogLater(context: Context)
}