package com.los3molineros.michelinstarrestaurants.domain

import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource

class MapRepositoryImpl(private val localRestaurantsDataSource: LocalRestaurantsDataSource): MapRepository {
    override suspend fun getStars(): List<String> = localRestaurantsDataSource.getStars()

    override suspend fun getRestaurantsByStars(star: String?) =  localRestaurantsDataSource.getRestaurantsByStars(star)
}