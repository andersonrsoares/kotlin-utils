package br.com.andersonsoares.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import br.com.andersonsoares.utils.showDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

open class LocationActivity : BaseActivity() {

    private val TAG = LocationActivity::class.java.simpleName
    private val REQUEST_CODE_RECOVER_PLAY_SERVICES = 111
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    /**
     * Constant used in the location settings dialog.
     */
    private val REQUEST_CHECK_SETTINGS = 0x1

    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Provides access to the Location Settings API.
     */
    private var mSettingsClient: SettingsClient? = null

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = null

    /**
     * Represents a geographical location.
     */
    private var mCurrentLocation: Location? = null

    private var mRequestingLocationUpdates = true

    fun isRequestingLocationUpdates(): Boolean {
        return mRequestingLocationUpdates
    }

    fun setRequestingLocationUpdates(mRequestingLocationUpdates: Boolean) {
        this.mRequestingLocationUpdates = mRequestingLocationUpdates
    }

    fun getCurrentLocation(): Location? {
        try {
            return mCurrentLocation
        } catch (ex: SecurityException) {
            Log.e("Location", "startLocationUpdate: ", ex)
        } catch (ex: Exception) {
            Log.e("Location", "startLocationUpdate: ", ex)
        }

        return null
    }


    open fun onLocationChanged(location: Location?) {
        mCurrentLocation = location
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        try {
            mFusedLocationClient!!.getLastLocation()
                    .addOnCompleteListener(this, object : OnCompleteListener<Location> {
                        override fun onComplete(task: Task<Location>) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mCurrentLocation = task.getResult()
                                if (mCurrentLocation != null) {
                                    Log.i("LOG", "latitude: " + mCurrentLocation!!.latitude)
                                    Log.i("LOG", "longitude: " + mCurrentLocation!!.longitude)
                                    onLocationChanged(mCurrentLocation)
                                }
                            } else {
                                Log.w(TAG, "getLastLocation:exception", task.getException())
                                //showDialog(getString(R.string.no_location_detected));
                            }
                        }
                    })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates()
        } else if (!checkPermissions()) {
            requestPermissions()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient?.let {
            it.removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this, object : OnCompleteListener<Void> {
                        override fun onComplete(task: Task<Void>) {
                            print("stopLocationUpdates")
                        }
                    })
        }

    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private fun startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.

        mSettingsClient?.let {
            it.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, object : OnSuccessListener<LocationSettingsResponse> {
                    override fun onSuccess(locationSettingsResponse: LocationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.")
                        try {
                            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback!!, Looper.myLooper())
                        }catch (e:Exception){
                            e.printStackTrace()
                        }



                    }
                })
                .addOnFailureListener(this, object : OnFailureListener {
                    override fun onFailure(e: Exception) {
                        val statusCode = (e as ApiException).getStatusCode()
                        when (statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(this@LocationActivity, REQUEST_CHECK_SETTINGS)
                                } catch (sie: IntentSender.SendIntentException) {
                                    Log.i(TAG, "PendingIntent unable to execute request.")
                                }

                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = getString(R.string.location_inadequate)
                                Log.e(TAG, errorMessage)
                                mRequestingLocationUpdates = false
                                showDialog(errorMessage)
                            }
                        }


                    }
                })
        }

    }


    private var locationInterval = 5000
    private var locationFastestInterval = 2000
    private var locationNumUpdates = 1
    private var locationPriority = LocationRequest.PRIORITY_HIGH_ACCURACY

    fun getLocationInterval(): Int {
        return locationInterval
    }

    fun setLocationInterval(locationInterval: Int) {
        this.locationInterval = locationInterval
    }

    fun getLocationFastestInterval(): Int {
        return locationFastestInterval
    }

    fun setLocationFastestInterval(locationFastestInterval: Int) {
        this.locationFastestInterval = locationFastestInterval
    }

    fun getLocationNumUpdates(): Int {
        return locationNumUpdates
    }

    fun setLocationNumUpdates(locationNumUpdates: Int) {
        this.locationNumUpdates = locationNumUpdates
    }

    fun getLocationPriority(): Int {
        return locationPriority
    }

    fun setLocationPriority(locationPriority: Int) {
        this.locationPriority = locationPriority
    }


    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.let {
            // Sets the desired interval for active location updates. This interval is
            // inexact. You may not receive updates at all if no location sources are available, or
            // you may receive them slower than requested. You may also receive updates faster than
            // requested if other applications are requesting location at a faster interval.
            it.setInterval(locationInterval.toLong())

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates faster than this value.
            it.setFastestInterval(locationFastestInterval.toLong())
            it.setNumUpdates(locationNumUpdates)
            it.setPriority(locationPriority)
            print("createLocationRequest")
        }

    }

    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                onLocationChanged(locationResult!!.getLastLocation())
            }
        }
    }

    /**
     * Uses a [com.google.android.gms.location.LocationSettingsRequest.Builder] to build
     * a [com.google.android.gms.location.LocationSettingsRequest] that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }


    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates")
                    startLocationUpdates()
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showDialog(getString(R.string.settings), getString(R.string.permission_denied_explanation),
                       callback = { dialog ->
                           // Build intent that displays the App settings screen.
                           val intent = Intent()
                           intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                           val uri = Uri.fromParts("package",
                                   BuildConfig.APPLICATION_ID, null)
                           intent.data = uri
                           intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                           startActivity(intent)

                       })

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(TAG, "User agreed to make required location settings changes.")
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    mRequestingLocationUpdates = true
                }
                Activity.RESULT_CANCELED -> {
                    Log.i(TAG, "User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
            }
        }
    }

}
