package com.nullgr.core.hardware

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author chernyshov.
 */
class NetworkChecker(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Checks if the device has any active internet connection.
     *
     * @return True if network connection enabled, otherwise returns false.
     */
    fun isInternetConnectionEnabled(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    /**
     * Checks if the device has active internet connection over wifi
     *
     * @return True if device connected to internet over wifi, otherwise returns false.
     */
    fun isConnectedOverWifi(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null
                && networkInfo.isConnectedOrConnecting
                && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Checks if the device has active internet connection over cellular
     *
     * @return True if device connected to internet over cellular, otherwise returns false.
     */
    fun isConnectedOverCellular(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null
                && networkInfo.isConnectedOrConnecting
                && networkInfo.type != ConnectivityManager.TYPE_WIFI
    }

    /**
     * Checks if the device has active internet connection over cellular and in roaming
     *
     * @return True if device connected to internet over over cellular and in roaming, otherwise returns false.
     */
    fun isInRoaming(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null
                && networkInfo.isConnectedOrConnecting
                && networkInfo.type != ConnectivityManager.TYPE_WIFI
                && networkInfo.isRoaming
    }
}