package com.los3molineros.michelinstarrestaurants.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Restaurant (
    val id: Int = 0,
    val rating: String = "",
    val year: String = "",
    val img: String = "",
    val name: String = "",
    val link: String = "",
    val location: String = "",
    val type: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
        )


@Entity
data class RestaurantEntity(
    @PrimaryKey
    val id: Int = 0,
    val rating: String = "",
    val year: String = "",
    val img: String = "",
    val name: String = "",
    val link: String = "",
    val location: String = "",
    val type: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

fun Restaurant.toRestaurantEntity() = RestaurantEntity(
    this.id,
    this.rating,
    this.year,
    this.img,
    this.name,
    this.link,
    this.location,
    this.type,
    this.lat,
    this.lng
)

fun RestaurantEntity.toRestaurant() = Restaurant(
    this.id,
    this.rating,
    this.year,
    this.img,
    this.name,
    this.link,
    this.location,
    this.type,
    this.lat,
    this.lng
)


fun List<RestaurantEntity>.toRestaurantList(): List<Restaurant> {
    val restaurantList = mutableListOf<Restaurant>()

    for(restaurantEntity in this) {
        restaurantList.add(restaurantEntity.toRestaurant())
    }

    return restaurantList
}