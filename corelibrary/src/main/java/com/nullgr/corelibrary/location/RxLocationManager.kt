package com.nullgr.corelibrary.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.support.annotation.RequiresPermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.nullgr.corelibrary.intents.launch
import com.nullgr.corelibrary.location.settings.LocationSettingsChangeEvent
import com.nullgr.corelibrary.location.settings.LocationSettingsResolveActivity
import com.nullgr.corelibrary.rx.SingletonRxBusProvider
import io.reactivex.Observable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider


/**
 * Created by Grishko Nikita on 01.02.18.
 *
 * Simple facade above [ReactiveLocationProvider] to receive location - both LastKnown and Updated.
 * Also it checks and handle location settings state.
 *
 * @property context - Any context
 * @property updatesInterval - interval between location updates in millis (default is 180000 ms.)
 * @property updateCount - integer number of updates or **null** if need to update all time
 * @constructor creates new instance of [RxLocationManager]
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

    /**
     * Check location settings status and request location updates
     * @return Observable that serves location updates
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun requestLocation(): Observable<Location> {
        return rxLocationProvider.checkLocationSettings(locationSettingsRequest)
                .flatMap {
                    when (it.status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> locationObservable()
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            if (!it.status.hasResolution())
                                Observable.just(LocationExtensions.EMPTY)
                            else
                                Observable.fromCallable {
                                    LocationSettingsResolveActivity.newInstance(context, it.status.resolution.intentSender)
                                            .launch(context)
                                }.flatMap {
                                    SingletonRxBusProvider.BUS.eventsObservable
                                            .filter { it is LocationSettingsChangeEvent }
                                            .map { it as LocationSettingsChangeEvent }
                                            .flatMap {
                                                when (it.resultCode) {
                                                    Activity.RESULT_OK -> locationObservable()
                                                    else -> Observable.just(LocationExtensions.EMPTY)
                                                }
                                            }
                                }
                        else -> Observable.just(LocationExtensions.EMPTY)
                    }
                }
    }

    @SuppressLint("MissingPermission")
    private fun locationObservable() = Observable.merge(rxLocationProvider.lastKnownLocation, rxLocationProvider.getUpdatedLocation(locationRequest))
}