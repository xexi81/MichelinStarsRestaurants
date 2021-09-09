package com.los3molineros.michelinstarrestaurants.data.remote

import com.los3molineros.michelinstarrestaurants.data.model.Restaurant

class RestaurantsDataSource(private val webservice: Webservice) {
    suspend fun getRestaurants(): List<Restaurant> = webservice.getStarRestaurants()
}