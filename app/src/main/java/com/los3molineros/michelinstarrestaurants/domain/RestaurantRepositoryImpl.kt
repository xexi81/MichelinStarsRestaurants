package com.los3molineros.michelinstarrestaurants.domain

import android.content.Context
import com.los3molineros.michelinstarrestaurants.common.CommonFunctions
import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import com.los3molineros.michelinstarrestaurants.data.model.toRestaurantEntity
import com.los3molineros.michelinstarrestaurants.data.remote.RestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.data.sharedPreferences.showRateDialogApp
import com.los3molineros.michelinstarrestaurants.data.sharedPreferences.updateNotRateApp
import com.los3molineros.michelinstarrestaurants.data.sharedPreferences.updateRateAppLater

class RestaurantRepositoryImpl(private val dataSource: RestaurantsDataSource, private val localRestaurantsDataSource: LocalRestaurantsDataSource): RestaurantRepository {
    override suspend fun getRestaurants(): List<Restaurant> {
        if (CommonFunctions.isNetworkAvailable()) {
            dataSource.getRestaurants().forEach {
                localRestaurantsDataSource.saveRestaurant(it.toRestaurantEntity())
            }
        }
        return localRestaurantsDataSource.getRestaurants()
    }


    override fun showDialogRateApp(context: Context): Boolean {
        val connects = showRateDialogApp(context)

        if (connects == 0) {
            return false
        } else {
            return  (showRateDialogApp(context) % 5 == 0 )
        }
    }

    override fun dontShowDialogNeverMore(context: Context) {
        updateNotRateApp(context)
    }

    override fun showDialogLater(context: Context) {
        updateRateAppLater(context)
    }

}