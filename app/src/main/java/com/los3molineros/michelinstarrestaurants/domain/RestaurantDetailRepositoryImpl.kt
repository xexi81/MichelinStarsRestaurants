package com.los3molineros.michelinstarrestaurants.domain

import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import com.los3molineros.michelinstarrestaurants.data.model.toRestaurant

class RestaurantDetailRepositoryImpl(private val dataSource: LocalRestaurantsDataSource ): RestaurantDetailRepository {
    override suspend fun getRestaurant(name: String): Restaurant = dataSource.getRestaurantByName(name).toRestaurant()

}