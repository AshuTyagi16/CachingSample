package com.ryuk.cachingsample.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtil @Inject constructor(
    private val connectivityManager: Lazy<ConnectivityManager>
) {

    fun isOnline(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.get()
                    .getNetworkCapabilities(connectivityManager.get().activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            return connectivityManager.get().activeNetworkInfo?.isConnected ?: false
        }
        return false
    }


}