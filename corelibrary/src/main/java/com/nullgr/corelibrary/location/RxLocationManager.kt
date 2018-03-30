package com.nullgr.corelibrary.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.support.annotation.RequiresPermission
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nullgr.corelibrary.BuildConfig
import com.nullgr.corelibrary.rx.asObservable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider


/**
 * Created by Grishko Nikita on 01.02.18.
 */
//TODO discuss this about implementation in MVVM
class RxLocationManager(private var context: Activity,
                        private val updatesInterval: Long = 180000,
                        private val updateCount: Int? = null) {

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
    }

    private val rxLocationProvider: ReactiveLocationProvider by lazy {
        ReactiveLocationProvider(context)
    }

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = updatesInterval
            updateCount?.let { numUpdates = it }
        }
    }

    private val locationSettingsRequest: LocationSettingsRequest by lazy {
        LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()
    }

    private var locationsSettingsStatus = BehaviorRelay.create<Status>()
    private var location = BehaviorRelay.create<Location>()

    private val compositeDestroy = CompositeDisposable()

    @SuppressLint("MissingPermission")
    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun requestLocation(): Observable<Location> {
        val disposableLocationStatus = checkLocationSettingsStatus()
                .subscribe(
                        {
                            when (it.statusCode) {
                                LocationSettingsStatusCodes.SUCCESS -> resolveLocationRequest()
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> tryToEnableLocationSettings(it)
                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> location.accept(LocationExtensions.EMPTY)
                            }
                        },
                        {
                            location.accept(LocationExtensions.EMPTY)
                        })
        compositeDestroy.add(disposableLocationStatus)
        return location.asObservable()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) locationsSettingsStatus.accept(Status(LocationSettingsStatusCodes.SUCCESS))
            else location.accept(LocationExtensions.EMPTY)
        }
    }

    fun onDestroy() {
        compositeDestroy.clear()
    }

    @SuppressLint("MissingPermission")
    private fun resolveLocationRequest() {
        val lastKnownLocationDisposable = rxLocationProvider.lastKnownLocation
                .subscribe {
                    location.accept(it.emptyIfNull())
                }

        val updatedLocationDisposable = rxLocationProvider.getUpdatedLocation(locationRequest)
                .subscribe({
                    location.accept(it.emptyIfNull())
                }, {
                    location.accept(LocationExtensions.EMPTY)
                })

        compositeDestroy.addAll(lastKnownLocationDisposable, updatedLocationDisposable)
    }

    private fun tryToEnableLocationSettings(status: Status) {
        try {
            status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS)
        } catch (e: IntentSender.SendIntentException) {
            if (BuildConfig.DEBUG)
                Log.e(this::class.java.simpleName, "PendingIntent unable to execute request. (%s)", e)
            location.accept(LocationExtensions.EMPTY)
        }
    }

    private fun checkLocationSettingsStatus(): Observable<Status> {
        val disposableSettings = rxLocationProvider.checkLocationSettings(locationSettingsRequest)
                .subscribe(
                        {
                            locationsSettingsStatus.accept(it.status
                                    ?: Status(LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE))
                        },
                        {
                            locationsSettingsStatus.accept(Status(LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE))
                        }
                )
        compositeDestroy.add(disposableSettings)
        return locationsSettingsStatus.asObservable()
    }
}