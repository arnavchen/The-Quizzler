package com.example.thequizzler.quiz.apiRepositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs

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
     * Finds the nearest place that matches a given keyword (e.g., "park", "Starbucks").
     * This is a suspend function, designed to be called from a coroutine.
     *
     * @param keyword The term to search for.
     * @param lat The user's current latitude.
     * @param lng The user's current longitude.
     * @return A `PlaceResult` if a match is found, or `null` if no permissions, no network, or no match exists.
     */
    suspend fun findNearestByKeyword(keyword: String, lat: Double, lng: Double): PlaceResult? {
        // The Places SDK requires location permissions. If not granted, we can't proceed.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("PlacesRepo", "Location permission not granted. Cannot fetch places.")
            return null
        }

        // Define the specific data fields we want from the API.
        // This is efficient and reduces data usage.
        val placeFields = listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        return try {
            val response = suspendCancellableCoroutine { continuation ->
                placesClient.findCurrentPlace(request).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result)
                    } else {
                        continuation.resumeWithException(task.exception ?: RuntimeException("Unknown Places API error"))
                    }
                }
            }

            // Process the list of likely places returned by the API.
            val bestMatch = response.placeLikelihoods
                .mapNotNull { placeLikelihood ->
                    val place = placeLikelihood.place
                    // Check if the place's name or type matches our keyword.
                    val nameMatches = place.name?.contains(keyword, ignoreCase = true) ?: false
                    val typeMatches = place.placeTypes?.any { it.contains(keyword, ignoreCase = true) } ?: false

                    if (nameMatches || typeMatches) {
                        // Calculate the distance from the user to this place.
                        val distance = FloatArray(1)
                        Location.distanceBetween(lat, lng, place.latLng!!.latitude, place.latLng!!.longitude, distance)
                        Pair(place, distance[0]) // Pair the place with its calculated distance.
                    } else {
                        null // This place is not a match.
                    }
                }
                .minByOrNull { it.second } // Find the pair with the smallest distance.

            // If a best match was found, convert it to our simple PlaceResult data class.
            bestMatch?.let { (place, distance) ->
                PlaceResult(
                    name = place.name!!,
                    latitude = place.latLng!!.latitude,
                    longitude = place.latLng!!.longitude,
                    distanceMeters = distance.toInt()
                )
            }
        } catch (e: Exception) {
            // If anything goes wrong (no network, invalid API key, etc.), log the error and return null.
            Log.e("PlacesRepo", "Failed to fetch places from API.", e)
            null
        }
    }
}
