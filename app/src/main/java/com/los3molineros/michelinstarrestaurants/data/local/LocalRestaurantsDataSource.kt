package com.los3molineros.michelinstarrestaurants.data.local

import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import com.los3molineros.michelinstarrestaurants.data.model.RestaurantEntity
import com.los3molineros.michelinstarrestaurants.data.model.toRestaurantList
import kotlinx.coroutines.flow.flow

class LocalRestaurantsDataSource(private val restaurantDao: RestaurantDao) {
    suspend fun getRestaurants(): List<Restaurant> {
        return restaurantDao.getAllRestaurants().toRestaurantList()
    }

    suspend fun saveRestaurant(restaurantEntity: RestaurantEntity) {
        restaurantDao.saveRestaurant(restaurantEntity)
    }

    suspend fun getStars(): List<String> {
        return restaurantDao.getDistinctStars()
    }

    suspend fun getRestaurantsByStars(rating: String?) = flow {
        emit(Resource.Loading())

        if (rating.isNullOrEmpty()) {
            emit(Resource.Success(restaurantDao.getAllRestaurants().toRestaurantList()))
        } else {
            emit(Resource.Success(restaurantDao.getRestaurantByRating(rating).toRestaurantList()))
        }
    }
}