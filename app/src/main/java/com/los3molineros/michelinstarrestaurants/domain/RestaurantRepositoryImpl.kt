package com.los3molineros.michelinstarrestaurants.domain

import com.los3molineros.michelinstarrestaurants.common.CommonFunctions
import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import com.los3molineros.michelinstarrestaurants.data.model.toRestaurantEntity
import com.los3molineros.michelinstarrestaurants.data.remote.RestaurantsDataSource

class RestaurantRepositoryImpl(private val dataSource: RestaurantsDataSource, private val localRestaurantsDataSource: LocalRestaurantsDataSource): RestaurantRepository {
    override suspend fun getRestaurants(): List<Restaurant> {
        if (CommonFunctions.isNetworkAvailable()) {
            dataSource.getRestaurants().forEach {
                localRestaurantsDataSource.saveRestaurant(it.toRestaurantEntity())
            }
        }
        return localRestaurantsDataSource.getRestaurants()
    }

}