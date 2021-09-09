package com.los3molineros.michelinstarrestaurants.common

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MichelinAddApp: Application() {
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)
    }
}