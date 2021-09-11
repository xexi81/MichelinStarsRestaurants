package com.los3molineros.michelinstarrestaurants.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

        viewModel.showAppDialog(this).observe(this) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data) {
                        showAlertDialogApp()
                    } else {
                        viewModel.showAppDialogLater(this)
                    }
                }
                is Resource.Failure -> {}
            }
        }

        initUI()
    }


    private fun initUI() {
        initSubscribers()
    }

    private fun initSubscribers() {
        viewModel.fetchRestaurants().observe(this) {
            when (it) {
                is Resource.Loading -> { binding.progressBar.visibility = View.VISIBLE}
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    startActivity(Intent(this, MapActivity::class.java))
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "${it.exception.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun showAlertDialogApp() {
        AppRatingDialog(this@MainActivity) { optionSelected ->
            when(optionSelected) {
                1 -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.los3molineros.michelinstarrestaurants")))
                    viewModel.notShowAppDialogNeverMore(this@MainActivity)
                }
                2 -> {
                    viewModel.showAppDialogLater(this@MainActivity)
                }
                3 -> {
                    viewModel.notShowAppDialogNeverMore(this@MainActivity)
                }
            }
        }
    }



}