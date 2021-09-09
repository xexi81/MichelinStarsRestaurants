package com.los3molineros.michelinstarrestaurants.domain

import com.los3molineros.michelinstarrestaurants.data.model.Restaurant

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
}