package com.los3molineros.michelinstarrestaurants.domain

import com.los3molineros.michelinstarrestaurants.data.model.Restaurant

interface RestaurantDetailRepository {
    suspend fun getRestaurant(name: String): Restaurant
}