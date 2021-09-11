package com.los3molineros.michelinstarrestaurants.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import com.los3molineros.michelinstarrestaurants.R
import com.los3molineros.michelinstarrestaurants.common.CommonFunctions
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.local.AppDataBase
import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.databinding.ActivityRestaurantDetailBinding
import com.los3molineros.michelinstarrestaurants.domain.RestaurantDetailRepositoryImpl
import com.los3molineros.michelinstarrestaurants.presentation.RestaurantDetailViewModel
import com.los3molineros.michelinstarrestaurants.presentation.RestaurantDetailViewModelFactory
import com.squareup.picasso.Picasso

class RestaurantDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRestaurantDetailBinding

    private val viewModel by viewModels<RestaurantDetailViewModel> {
        RestaurantDetailViewModelFactory(
            RestaurantDetailRepositoryImpl(
                LocalRestaurantsDataSource(AppDataBase.getDatabase(this).restaurantDao())
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        initFormats()
        initListeners()
        initBanner()
        initSubscribers()
    }

    private fun initFormats() {
        binding.txtName.typeface = CommonFunctions.returnTypefaceKimbalt(this)
        binding.txtLink.typeface = CommonFunctions.returnTypefaceKingthings(this)
        binding.txtLocation.typeface = CommonFunctions.returnTypefaceKingthings(this)
        binding.txtYear.typeface = CommonFunctions.returnTypefaceKingthings(this)
    }

    private fun initListeners() {
    }

    private fun initBanner() {
        val mBanner = findViewById<AdView>(R.id.banner)
        val adRequest = AdRequest.Builder().build()
        mBanner.loadAd(adRequest)
    }

    private fun initSubscribers() {
        val name = intent.extras?.getString("NAME") ?: ""

        viewModel.fetchRestaurant(name).observe(this) { restaurant ->
            when (restaurant) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.txtName.text = restaurant.data.name
                    binding.txtYear.text = restaurant.data.year
                    binding.txtLocation.text = restaurant.data.location
                    binding.txtLink.text = restaurant.data.link

                    Picasso.get().load(restaurant.data.img).into(binding.ivBackground)

                    binding.txtLink.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.data.link)))
                    }

                    when(restaurant.data.rating) {
                        "1" -> {
                            binding.ivStar2.visibility = View.INVISIBLE
                            binding.ivStar3.visibility = View.INVISIBLE
                            binding.ivStar4.visibility = View.INVISIBLE
                            binding.ivStar5.visibility = View.INVISIBLE
                        }
                        "2" -> {
                            binding.ivStar2.visibility = View.VISIBLE
                            binding.ivStar3.visibility = View.INVISIBLE
                            binding.ivStar4.visibility = View.INVISIBLE
                            binding.ivStar5.visibility = View.INVISIBLE
                        }
                        "3" -> {
                            binding.ivStar2.visibility = View.VISIBLE
                            binding.ivStar3.visibility = View.VISIBLE
                            binding.ivStar4.visibility = View.INVISIBLE
                            binding.ivStar5.visibility = View.INVISIBLE
                        }
                        "4" -> {
                            binding.ivStar2.visibility = View.VISIBLE
                            binding.ivStar3.visibility = View.VISIBLE
                            binding.ivStar4.visibility = View.VISIBLE
                            binding.ivStar5.visibility = View.INVISIBLE
                        }
                        "5" -> {
                            binding.ivStar2.visibility = View.VISIBLE
                            binding.ivStar3.visibility = View.VISIBLE
                            binding.ivStar4.visibility = View.VISIBLE
                            binding.ivStar5.visibility = View.VISIBLE
                        }
                        else -> {
                            binding.ivStar2.visibility = View.INVISIBLE
                            binding.ivStar3.visibility = View.INVISIBLE
                            binding.ivStar4.visibility = View.INVISIBLE
                            binding.ivStar5.visibility = View.INVISIBLE
                        }
                    }
                }
                is Resource.Failure -> {
                    Snackbar.make(binding.root, "${restaurant.exception.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}