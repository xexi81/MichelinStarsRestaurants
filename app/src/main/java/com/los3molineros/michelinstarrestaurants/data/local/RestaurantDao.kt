package com.los3molineros.michelinstarrestaurants.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.los3molineros.michelinstarrestaurants.data.model.RestaurantEntity

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM RestaurantEntity")
    suspend fun getAllRestaurants(): List<RestaurantEntity>

    @Query("SELECT DISTINCT rating FROM RestaurantEntity")
    suspend fun getDistinctStars(): List<String>

    @Query("SELECT * FROM RestaurantEntity WHERE rating = :rating")
    suspend fun getRestaurantByRating(rating: String): List<RestaurantEntity>

    @Query("SELECT * FROM RestaurantEntity WHERE name = :name")
    suspend fun getRestaurantByName(name: String): RestaurantEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRestaurant(restaurant: RestaurantEntity)
}