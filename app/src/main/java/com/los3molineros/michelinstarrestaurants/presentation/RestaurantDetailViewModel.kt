package com.los3molineros.michelinstarrestaurants.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.domain.RestaurantDetailRepository
import kotlinx.coroutines.Dispatchers

class RestaurantDetailViewModel(private val repo: RestaurantDetailRepository): ViewModel() {
    fun fetchRestaurant(name: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            emit(Resource.Success(repo.getRestaurant(name)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}


class RestaurantDetailViewModelFactory(private val repo: RestaurantDetailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RestaurantDetailRepository::class.java).newInstance(repo)
    }
}