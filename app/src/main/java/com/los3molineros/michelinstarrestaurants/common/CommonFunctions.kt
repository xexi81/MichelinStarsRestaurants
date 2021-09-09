package com.los3molineros.michelinstarrestaurants.common

import android.util.Log
import com.los3molineros.michelinstarrestaurants.common.AppConstants.TAG_LOG
import kotlinx.coroutines.coroutineScope
import java.net.InetSocketAddress
import java.net.Socket

object CommonFunctions {

    fun debugLog(tag: String = TAG_LOG, description: String) {
        Log.d(tag, description)
    }

    suspend fun isNetworkAvailable() = coroutineScope {
        return@coroutineScope try {
            val sock = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(socketAddress, 2000)
            sock.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}