package com.example.myapplication.ui.maps

import android.app.Application
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.util.rememberMapViewWithLifecycle
import com.example.myapplication.viewmodels.ReminderViewModel
import com.example.myapplication.viewmodels.ReminderViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import java.util.*
import kotlinx.coroutines.launch

@Composable
fun ReminderLocation(
    app: Application,
    navController: NavController,
    locationX: String,
    locationY: String,
    viewModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(app)),
    content: String?
) {
    val mapView: MapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val viewState by viewModel.state.collectAsState()

    AndroidView({mapView}) {
        coroutineScope.launch {
            val map = mapView.awaitMap()
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isScrollGesturesEnabled = true

            val location = LatLng(locationX.toDouble(), locationY.toDouble())
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    17f
                )
            )
            setCurrentPosMarker(map, content!!, locationX, locationY)
            setMapLongClick(map, navController, viewState.reminders)
        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController,
    reminders: List<Reminder>
) {
    map.setOnMapLongClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.6f, Lng: %2$.6f",
            latlng.latitude,
            latlng.longitude
        )
        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("location_data", latlng)
        }

        reminders.forEach{reminder->
            if(getDistanceToDestination(latlng.latitude, latlng.longitude,
                    reminder.locationX!!, reminder.locationY!!)< 5000){
                val newLatLng = LatLng(reminder.locationX!!, reminder.locationY!!)

                map.addMarker(
                    MarkerOptions().position(newLatLng).title(reminder.content).snippet(snippet)
                ).apply {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("location_data", latlng)
                }
            }
        }
    }
}

private fun setCurrentPosMarker(
    map: GoogleMap,
    content: String,
    locationX: String,
    locationY: String){

    val latlng = LatLng(locationX.toDouble(), locationY.toDouble())
    val snippet = String.format(
        Locale.getDefault(),
        "Lat: %1$.6f, Lng: %2$.6f",
        latlng.latitude,
        latlng.longitude
    )

    map.addMarker(
        MarkerOptions().position(latlng).title(content).snippet(snippet)
    )
}
fun getDistanceToDestination(startLocationX: Double, startLocationY: Double,
endLocationX: Double, endLocationY: Double): Float {
    val results: FloatArray = floatArrayOf(elements = FloatArray(3))

    Location.distanceBetween(
       startLocationX, startLocationY,
        endLocationX, endLocationY,
        results
    )
    println("Distance from current to reminder location: ${results[0]} meters")
    return results[0]
}