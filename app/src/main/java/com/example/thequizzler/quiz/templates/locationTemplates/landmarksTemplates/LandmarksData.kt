package com.example.thequizzler.quiz.templates.locationTemplates.landmarksTemplates

/**
 * A static data object containing a curated list of famous landmarks
 * with their names and geographic coordinates.
 */
object LandmarksData {

    // A simple data class to hold the landmark information.
    data class Landmark(val name: String, val latitude: Double, val longitude: Double)

    val landmarks = listOf(
        // USA
        Landmark("Statue of Liberty", 40.6892, -74.0445),
        Landmark("Golden Gate Bridge", 37.8199, -122.4783),
        Landmark("Mount Rushmore", 43.8791, -103.4591),
        Landmark("The White House", 38.8977, -77.0365),
        Landmark("Space Needle", 47.6205, -122.3493),
        Landmark("Hollywood Sign", 34.1341, -118.3215),
        Landmark("Gateway Arch", 38.6247, -90.1848),

        // Europe
        Landmark("Eiffel Tower", 48.8584, 2.2945),
        Landmark("Colosseum", 41.8902, 12.4922),
        Landmark("Big Ben", 51.5007, -0.1246),
        Landmark("Acropolis of Athens", 37.9715, 23.7257),
        Landmark("Neuschwanstein Castle", 47.5576, 10.7498),
        Landmark("Brandenburg Gate", 52.5163, 13.3777),
        Landmark("Sagrada Família", 41.4036, 2.1744),

        // Asia
        Landmark("Taj Mahal", 27.1751, 78.0421),
        Landmark("Great Wall of China", 40.4319, 116.5704),
        Landmark("Fushimi Inari Shrine", 34.9671, 135.7726),
        Landmark("Burj Khalifa", 25.1972, 55.2744),
        Landmark("Petronas Towers", 3.1579, 101.7116),

        // Other
        Landmark("Sydney Opera House", -33.8568, 151.2153),
        Landmark("Machu Picchu", -13.1631, -72.5450),
        Landmark("Christ the Redeemer", -22.9519, -43.2105),
        Landmark("Pyramids of Giza", 29.9792, 31.1342)
    )
}
