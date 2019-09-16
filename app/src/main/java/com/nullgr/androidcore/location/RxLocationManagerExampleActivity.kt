package com.nullgr.androidcore.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.nullgr.androidcore.R
import com.nullgr.core.rx.location.RxLocationManager
import com.nullgr.core.rx.location.isEmpty
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_rx_location_manager_example.*

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxLocationManagerExampleActivity : AppCompatActivity() {

    // Implementation steps:
    //
    // 1) Add LocationSettingsResolveActivity to your manifest file
    // <activity android:name="com.nullgr.core.rx.location.settings.LocationSettingsResolveActivity" />
    // 2) Create or inject instance of RxLocationManger and setup configuration (updates count and updates interval)
    // 3) Check ACCESS_FINE_LOCATION permission
    // 4) Call requestLocation() which returns Observable<Location>
    // 5) Don't forget unsubscribe in onDestroy (this will automatically stops location updates),
    // or in the case when location updates are no longer required
    // 6) Note in that case if location updates is failed in some reasons, will be returned LocationExtensions.EMPTY_LOCATION instance,
    // you can check is with Location.isEmpty method as shown below

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_location_manager_example)

        val rxLocationManager = RxLocationManager(this, 1100, 10)
        var updatesCount = 0

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            disposable = rxLocationManager.requestLocation()
                    .subscribe {
                        locationLog.text = if (!it.isEmpty()) {
                            getString(R.string.mask_location_result, ++updatesCount, it.toString())
                        } else {
                            getString(R.string.error_empty_location)
                        }
                    }
        } else {
            locationLog.text = getString(R.string.permission_not_granted, Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::disposable.isInitialized)
            disposable.dispose()
    }
}