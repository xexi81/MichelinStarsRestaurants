package com.los3molineros.michelinstarrestaurants.data.remote

import com.google.gson.GsonBuilder
import com.los3molineros.michelinstarrestaurants.common.AppConstants
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Webservice {
    @GET("restaurants")
    suspend fun getStarRestaurants(): List<Restaurant>
}


object RetrofitClient {
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(Webservice::class.java)
    }
}