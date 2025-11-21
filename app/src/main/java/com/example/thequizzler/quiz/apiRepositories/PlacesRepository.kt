package com.example.thequizzler.quiz.apiRepositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.google.android.libraries.places.api.net.SearchNearbyResponse
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


/**
 * Minimal, clean data class for a place result. This is what the rest of the app will use,
 * abstracting away the complexity of the Google Places SDK's 'Place' object.
 * The distance is optional, as it may be computed separately.
 */
data class PlaceResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Int? = null
)

/**
 * A modern, 2025-compliant implementation of a repository that uses the Google Places SDK for Android.
 * This class is the single source of truth for fetching place data from the Google API.
 *
 * @param context The application context, required for initializing the Places SDK.
 */
class PlacesRepository(private val context: Context) {

    // The client for interacting with the Google Places API. It's initialized lazily.
    private val placesClient: PlacesClient

    init {
        // Define a variable to hold the Places API key.
        val apiKey = "AIzaSyCq65qDVQdpMelrUSg7enm1TGnRs3coPeQ"

        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(context, apiKey)

        // Create a new PlacesClient instance
        placesClient = Places.createClient(context);

    }

    /**
     * Finds the nearest place that matches a specific NAME (e.g., "Starbucks").
     * This uses a Text Search, which is optimized for name queries.
     */
    suspend fun findNearestPlaceByFranchiseName(name: String, lat: Double, lng: Double): PlaceResult? {
        val placeFields = listOf(Place.Field.DISPLAY_NAME, Place.Field.LOCATION)
        val center = LatLng(lat, lng)

        // Build a Text Search request.
        val request = SearchByTextRequest.builder(name, placeFields)
            .setLocationRestriction(CircularBounds.newInstance(center, 40000.0)) // About 25 miles
            .setMaxResultCount(1)
            .setRankPreference(SearchByTextRequest.RankPreference.DISTANCE)
            .build()

        return try {
            // Call the placesClient.searchByText() method.
            val response = suspendCancellableCoroutine<SearchByTextResponse> { continuation ->
                placesClient.searchByText(request).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        continuation.resumeWithException(task.exception ?: RuntimeException("Unknown Places API error"))
                    }
                }
            }
            val place = response.places.firstOrNull()

            if(place == null) {
                return null
            }

            val distance = FloatArray(1)
            Location.distanceBetween(lat, lng, place.location!!.latitude, place.location!!.longitude, distance);

            PlaceResult(
                name = place.displayName!!,
                latitude = place.location!!.latitude,
                longitude = place.location!!.longitude,
                distanceMeters = distance[0].toInt()
            )
        } catch (e: Exception) {
            Log.e("PlacesRepo", "Failed to fetch places from Text Search API.", e)
            null
        }
    }

    /*
     * Finds the nearest place that matches a given TYPE (e.g., "park", "museum").
     * This uses a Nearby Search, which is optimized for category queries.
     */
    suspend fun findNearestPlaceByType(type: String, lat: Double, lng: Double): PlaceResult? {
        val placeFields = listOf(Place.Field.DISPLAY_NAME, Place.Field.LOCATION)
        val center = LatLng(lat, lng)

        // Build a Nearby Search request.
        val request = SearchNearbyRequest.builder(CircularBounds.newInstance(center, 40000.0), placeFields)
            .setIncludedTypes(listOf(type))
            .setMaxResultCount(1)
            .setRankPreference(SearchNearbyRequest.RankPreference.DISTANCE)
            .build()

        return try {
            // Call the placesClient.searchNearby() method.
            val response = suspendCancellableCoroutine<SearchNearbyResponse> { continuation ->
                placesClient.searchNearby(request).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        continuation.resumeWithException(task.exception ?: RuntimeException("Unknown Places API error"))
                    }
                }
            }

            val place = response.places.firstOrNull()

            if(place == null) {
                return null
            }

            val distance = FloatArray(1)
            Location.distanceBetween(lat, lng, place.location!!.latitude, place.location!!.longitude, distance);

            PlaceResult(
                name = place.displayName!!,
                latitude = place.location!!.latitude,
                longitude = place.location!!.longitude,
                distanceMeters = distance[0].toInt()
            )
        } catch (e: Exception) {
            Log.e("PlacesRepo", "Failed to fetch places from Nearby Search API.", e)
            null
        }
    }
}
