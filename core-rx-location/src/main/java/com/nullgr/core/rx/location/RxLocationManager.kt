package com.nullgr.core.rx.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.support.annotation.RequiresPermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.nullgr.core.rx.RxBus
import com.nullgr.core.rx.SingletonRxBusProvider
import com.nullgr.core.intents.rxresult.RxActivityResult
import com.nullgr.core.intents.rxresult.RxResolveResultActivity
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
                                Observable.just(EMPTY_LOCATION)
                            else
                                Observable.fromCallable {
                                    val intent = RxResolveResultActivity.newInstance(context,
                                            it.status.resolution.intentSender)
                                    context.startActivity(intent)
                                }.flatMap {
                                    SingletonRxBusProvider.BUS.observable(RxBus.Keys.SINGLE)
                                            .filter { it is RxActivityResult }
                                            .map { it as RxActivityResult }
                                            .flatMap {
                                                when (it.resultCode) {
                                                    Activity.RESULT_OK -> locationObservable()
                                                    else -> Observable.just(EMPTY_LOCATION)
                                                }
                                            }
                                }
                        else -> Observable.just(EMPTY_LOCATION)
                    }
                }
    }

    @SuppressLint("MissingPermission")
    private fun locationObservable() = Observable.merge(rxLocationProvider.lastKnownLocation, rxLocationProvider.getUpdatedLocation(locationRequest))
}