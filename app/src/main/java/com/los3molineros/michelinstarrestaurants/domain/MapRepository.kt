package com.los3molineros.michelinstarrestaurants.domain

import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    suspend fun getStars(): List<String>
    suspend fun getRestaurantsByStars(star: String?): Flow<Resource<List<Restaurant>>>
}