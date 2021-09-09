package com.los3molineros.michelinstarrestaurants.presentation

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.model.Restaurant
import com.los3molineros.michelinstarrestaurants.domain.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapViewModel(private val repo: MapRepository) : ViewModel() {
    private val _restaurants = MutableLiveData<Resource<List<Restaurant>>>()
    val restaurants: LiveData<Resource<List<Restaurant>>> get() = _restaurants

    private val _latLng = MutableLiveData<LatLng>()
    val latLng: LiveData<LatLng> get() = _latLng

    fun fetchStars() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            emit(Resource.Success(repo.getStars()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


    fun fetchRestaurantsByStars(stars: String?) {
        viewModelScope.launch {
            repo.getRestaurantsByStars(stars).collect {
                _restaurants.value = it
            }
        }
    }


    fun setCurrentLocation(lat: Double, lng: Double) {
        _latLng.value = LatLng(lat, lng)
    }


}


class MapViewModelFactory(private val repo: MapRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MapRepository::class.java).newInstance(repo)
    }
}