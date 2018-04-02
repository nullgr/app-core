package com.nullgr.corelibrary.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.support.annotation.RequiresPermission
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nullgr.corelibrary.BuildConfig
import com.nullgr.corelibrary.location.settings.LocationSettingsChangeEvent
import com.nullgr.corelibrary.location.settings.LocationSettingsResolveActivity
import com.nullgr.corelibrary.rx.SingletonRxBusProvider
import com.nullgr.corelibrary.rx.asObservable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider


/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxLocationManager(private var context: Context,
                        private val updatesInterval: Long = 180000,
                        private val updateCount: Int? = null) {

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
                .setAlwaysShow(true)
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

    fun unBind() {
        compositeDestroy.clear()
    }

    fun getSubscribers(): CompositeDisposable {
        return compositeDestroy
    }

    fun getLocation(): Location {
        return if (location.hasValue()) location.value.emptyIfNull() else LocationExtensions.EMPTY
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
            if (status.hasResolution()) {
                context.startActivity(Intent(context, LocationSettingsResolveActivity::class.java)
                        .apply {
                            putExtra(LocationSettingsResolveActivity.EXTRA_INTENT_SENDER, status.resolution.intentSender)
                            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        })
                val eventDisposable = SingletonRxBusProvider.BUS.eventsObservable
                        .filter { it is LocationSettingsChangeEvent }
                        .map { it as LocationSettingsChangeEvent }
                        .subscribe(
                                {
                                    processSettingChangeResult(it)
                                },
                                {
                                    location.accept(LocationExtensions.EMPTY)
                                })
                compositeDestroy.add(eventDisposable)
            }
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG)
                Log.e(this::class.java.simpleName, "Unable to start settings resolve activity. (%s)", e)
            location.accept(LocationExtensions.EMPTY)
        }
    }

    private fun processSettingChangeResult(result: LocationSettingsChangeEvent) {
        if (result.resultCode == Activity.RESULT_OK) locationsSettingsStatus.accept(Status(LocationSettingsStatusCodes.SUCCESS))
        else location.accept(LocationExtensions.EMPTY)
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