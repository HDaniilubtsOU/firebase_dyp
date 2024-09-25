package com.example.firebase_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.firebase_test.databinding.FragmentWysBinding
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.common.location.compat.permissions.PermissionsListener
import com.mapbox.common.location.compat.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.plugin.viewport.viewport


class WysFragment : Fragment(), LocationEngineCallback<LocationEngineResult> {

    companion object {
        private const val TAG = "MapFragment"
        private const val REQUEST_LOCATION_UPDATE_INTERVAL = 1000L
    }

    private lateinit var binding: FragmentWysBinding
    private lateinit var mapView: MapView
    private val locationPermissionGranted: Boolean
        get() = PermissionsManager.areLocationPermissionsGranted(requireContext())
    private val permissionsManager = PermissionsManager(object : PermissionsListener {
        override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {}

        override fun onPermissionResult(granted: Boolean) {
            if (granted) {
                // Разрешения получены, можно выполнять логику, требующую разрешений
                activateLocationComponent()
            } else {
                // Пользователь отказал в предоставлении разрешений, можно предпринять дальнейшие действия
            }
        }
    })


    private lateinit var coordinatesTextView: TextView
    private lateinit var centerMarker: ImageView
    private var markerVisible = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMap()
        verifyLocationPermission()
        setLocationButtonClickListener()


        binding.location2.setOnClickListener {
            setLocationButtonClickListener2()

//            val center = mapView.getMapboxMap().cameraState.center
//            updateCoordinates(center)
//            Toast.makeText(context, "Coordinates: ${center.latitude()}, ${center.longitude()}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpMap() {
        mapView = binding.mapView
        mapView.scalebar.enabled = false

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder().center(Point.fromLngLat(21.010872, 52.220370)).pitch(0.0).zoom(10.0).bearing(0.0).build()
        )

//        coordinatesTextView = binding.coordinatesTextView
////        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
////            enableLocationComponent()
////        }
//
//        mapView.getMapboxMap().addOnCameraChangeListener {
//            val center = mapView.getMapboxMap().cameraState.center
//            updateCoordinates(center)
//        }

    }

    private fun verifyLocationPermission() {
        if (locationPermissionGranted) {
            // Разрешения на местоположение уже предоставлены
            // Можно выполнить логику, требующую разрешений
            activateLocationComponent()
        } else {
            permissionsManager.requestLocationPermissions(requireActivity())
        }
    }
//    private fun verifyLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//            return
//        }
//    }

    @SuppressLint("MissingPermission")
    private fun activateLocationComponent() {
        // логика активации компонента местоположения
        with(mapView) {
//                location.locationPuck = createDefault2DPuck(withBearing = true)
            location.enabled = true
//                location.puckBearing = PuckBearing.COURSE
            viewport.transitionTo(
                targetState = viewport.makeFollowPuckViewportState(), transition = viewport.makeImmediateViewportTransition()
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationButtonClickListener() {
        binding.location.setOnClickListener {
            if (!locationPermissionGranted) return@setOnClickListener
            val locationEngine = LocationEngineProvider
                .getBestLocationEngine(requireContext())
            locationEngine.requestLocationUpdates(
                LocationEngineRequest.Builder(REQUEST_LOCATION_UPDATE_INTERVAL)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .build(),
                this@WysFragment,
                Looper.getMainLooper()
            )
        }
    }

    override fun onSuccess(result: LocationEngineResult?) {
        val location = result?.lastLocation
        if (location == null) {
            Log.w(TAG, "lastLocation is null")
            return
        }
        val latitude = location.latitude
        val longitude = location.longitude
        showMessage("latitude: $latitude; longitude: $longitude")
    }

    private fun showMessage(message: String) {
        Log.d(TAG, message)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(exception: Exception) {
        showMessage(exception.localizedMessage ?: "Nie udało się pobrać lokalizacji urządzenia.")
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_wys, container, false)
//    }


//    private fun setLocationButtonClickListener2() {
//        centerMarker = binding.centerMarker
////        coordinatesTextView = binding.coordinatesTextView
//        binding.location2.setOnClickListener {
//            val center = mapView.getMapboxMap().cameraState.center
//            updateCoordinates(center)
//            Toast.makeText(context, "Coordinates: ${center.latitude()}, ${center.longitude()}", Toast.LENGTH_SHORT).show()
//        }
//    }
    private fun setLocationButtonClickListener2() {
        centerMarker = binding.centerMarker
        coordinatesTextView = binding.coordinatesTextView
        binding.location2.setOnClickListener {

            mapView.getMapboxMap().addOnCameraChangeListener {
                val center = mapView.getMapboxMap().cameraState.center
                updateCoordinates(center)
            }

            markerVisible = !markerVisible
            if (markerVisible) {
                val center = mapView.getMapboxMap().cameraState.center
                updateCoordinates(center)
                centerMarker.visibility = View.VISIBLE
                coordinatesTextView.visibility = View.VISIBLE

                Toast.makeText(context, "Coordinates: ${center.latitude().toString().take(10)}, ${center.longitude().toString().take(10)}", Toast.LENGTH_SHORT).show()
            } else {
                centerMarker.visibility = View.GONE
                coordinatesTextView.visibility = View.GONE
            }
        }
    }

//    private fun enableLocationComponent() {
//        val locationComponentPlugin = mapView.location
//        locationComponentPlugin.updateSettings {
//            enabled = true
//            pulsingEnabled = true
//        }
//    }

    private fun updateCoordinates(center: Point) {
        coordinatesTextView.text = "Lat: ${center.latitude().toString().take(10)}, Lng: ${center.longitude().toString().take(10)}"
    }

//    companion object {
//        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
//    }

}