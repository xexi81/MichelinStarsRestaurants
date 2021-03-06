package com.los3molineros.michelinstarrestaurants.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.domain.RestaurantRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repo: RestaurantRepository): ViewModel() {
    fun fetchRestaurants(stars: String? = null) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            if (stars.isNullOrEmpty()) emit(Resource.Success(repo.getRestaurants()))
            else emit(Resource.Success(repo.getRestaurants().filter { it.rating == stars }))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun showAppDialog(context: Context) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            emit(Resource.Success(repo.showDialogRateApp(context)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun notShowAppDialogNeverMore(context: Context) {
        repo.dontShowDialogNeverMore(context)
    }

    fun showAppDialogLater(context: Context) {
        repo.showDialogLater(context)
    }
}



class MainViewModelFactory(private val repo: RestaurantRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RestaurantRepository::class.java).newInstance(repo)
    }
}