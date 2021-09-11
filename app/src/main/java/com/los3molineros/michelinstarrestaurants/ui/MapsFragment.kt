package com.los3molineros.michelinstarrestaurants.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.los3molineros.michelinstarrestaurants.R
import com.los3molineros.michelinstarrestaurants.common.AppConstants.LAT_DEFAULT
import com.los3molineros.michelinstarrestaurants.common.AppConstants.LNG_DEFAULT
import com.los3molineros.michelinstarrestaurants.common.AppConstants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.los3molineros.michelinstarrestaurants.common.Resource
import com.los3molineros.michelinstarrestaurants.data.local.AppDataBase
import com.los3molineros.michelinstarrestaurants.data.local.LocalRestaurantsDataSource
import com.los3molineros.michelinstarrestaurants.domain.MapRepositoryImpl
import com.los3molineros.michelinstarrestaurants.presentation.MapViewModel
import com.los3molineros.michelinstarrestaurants.presentation.MapViewModelFactory


class MapsFragment : Fragment(R.layout.fragment_maps) {
    private var locationPermissionGranted = false


    private val viewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(
            MapRepositoryImpl(
                LocalRestaurantsDataSource(AppDataBase.getDatabase(requireContext()).restaurantDao())
            )
        )
    }

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.clear()

        viewModel.restaurants.observe(this) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    for (restaurant in it.data) {
                        googleMap.addMarker(
                            MarkerOptions().position(LatLng(restaurant.lat, restaurant.lng))
                                .title(restaurant.name)
                        )
                    }

                    getLocationPermission()
                    viewModel.latLng.observe(this) { latLng ->
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            latLng, 8f))
                    }
                }
                is Resource.Failure -> {
                    Snackbar.make(requireView(), "Map failed", Snackbar.LENGTH_LONG)
                }
            }
        }


        googleMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
            intent.putExtra("NAME", marker.title)
            startActivity(intent)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }


    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        getDeviceLocation()
    }


    private fun getDeviceLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            viewModel.setCurrentLocation(lastKnownLocation.latitude, lastKnownLocation.longitude)
                        }
                    } else {
                        viewModel.setCurrentLocation(LAT_DEFAULT, LNG_DEFAULT)
                    }
                }
            } else {
                viewModel.setCurrentLocation(LAT_DEFAULT, LNG_DEFAULT)
            }
        } catch (e: SecurityException) {
            viewModel.setCurrentLocation(LAT_DEFAULT, LNG_DEFAULT)
        }
    }

}