package com.los3molineros.michelinstarrestaurants.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.local.AppDataBase
import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.data.remote.RestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.data.remote.RetrofitClient
import com.los3molineros.michelinstarrestaurants.databinding.ActivityMainBinding
import com.los3molineros.michelinstarrestaurants.domain.RestaurantRepositoryImpl
import com.los3molineros.michelinstarrestaurants.presentation.MainViewModel
import com.los3molineros.michelinstarrestaurants.presentation.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(
            RestaurantRepositoryImpl(
                RestaurantsDataSource(RetrofitClient.webservice),
                LocalRestaurantsDataSource(AppDataBase.getDatabase(this).restaurantDao())
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }


    private fun initUI() {
        initSubscribers()
    }

    private fun initSubscribers() {
        viewModel.fetchRestaurants().observe(this) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> startActivity(Intent(this, MapActivity::class.java))
                is Resource.Failure -> {
                    Snackbar.make(binding.root, "${it.exception.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}