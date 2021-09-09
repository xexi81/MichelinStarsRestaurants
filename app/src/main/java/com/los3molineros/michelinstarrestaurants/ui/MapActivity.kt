package com.los3molineros.michelinstarrestaurants.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import com.los3molineros.michelinstarrestaurants.R
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.local.AppDataBase
import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.domain.MapRepositoryImpl
import com.los3molineros.michelinstarrestaurants.presentation.MapViewModel
import com.los3molineros.michelinstarrestaurants.presentation.MapViewModelFactory

class MapActivity : AppCompatActivity() {
    private var stars: String? = null

    private val viewModel by viewModels<MapViewModel> {
        MapViewModelFactory(
            MapRepositoryImpl(
                LocalRestaurantsDataSource(AppDataBase.getDatabase(this).restaurantDao())
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initUI()
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        menu?.let {
            menu.findItem(R.id.menu_show_all).isVisible = true
            menu.findItem(R.id.menu_1star).isVisible = false
            menu.findItem(R.id.menu_2stars).isVisible = false
            menu.findItem(R.id.menu_3stars).isVisible = false
            menu.findItem(R.id.menu_4stars).isVisible = false
            menu.findItem(R.id.menu_5stars).isVisible = false
        }


        menu?.let { menuItem ->
            viewModel.fetchStars().observe(this) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        for (star in it.data) {
                            when (star) {
                                "1" -> menuItem.findItem(R.id.menu_1star).isVisible = true
                                "2" -> menuItem.findItem(R.id.menu_2stars).isVisible = true
                                "3" -> menuItem.findItem(R.id.menu_3stars).isVisible = true
                                "4" -> menuItem.findItem(R.id.menu_4stars).isVisible = true
                                "5" -> menuItem.findItem(R.id.menu_5stars).isVisible = true
                                else -> {}//Snackbar.make(binding.root, "error showing stars", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                    is Resource.Failure -> {
                        val contextView = findViewById<View>(R.id.content)
                        Snackbar.make(
                            contextView, "${it.exception.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId) {
            R.id.menu_1star -> viewModel.fetchRestaurantsByStars("1")
            R.id.menu_2stars -> viewModel.fetchRestaurantsByStars("2")
            R.id.menu_3stars -> viewModel.fetchRestaurantsByStars("3")
            R.id.menu_4stars -> viewModel.fetchRestaurantsByStars("4")
            R.id.menu_5stars -> viewModel.fetchRestaurantsByStars("5")
            R.id.menu_show_all -> viewModel.fetchRestaurantsByStars(null)
        }

        refreshCurrentFragment()

        return true
    }


    private fun initUI() {
        viewModel.fetchRestaurantsByStars("3")

        initFormats()
        initListeners()
        initBanner()
        initSubscribers()
    }

    private fun initFormats() {
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        mToolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_grade_24);
    }

    private fun initListeners() {
    }

    private fun initBanner() {
        val mBanner = findViewById<AdView>(R.id.banner)
        val adRequest = AdRequest.Builder().build()
        mBanner.loadAd(adRequest)
    }

    private fun initSubscribers() {
    }


    private fun refreshCurrentFragment() {
        val navController = findNavController(R.id.nav_host_fragment)
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!, true)
        navController.navigate(id)
    }
}