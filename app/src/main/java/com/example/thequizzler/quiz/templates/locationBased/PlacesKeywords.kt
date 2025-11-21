package com.example.thequizzler.quiz.templates.locationBased

/**
 * A centralized object to store shared lists of keywords for location-based questions.
 */
object PlacesKeywords {

    val franchises = listOf(
        "Starbucks", "McDonald's", "Chipotle", "Chick-fil-A",
        "Pizza Hut", "Subway", "Panda Express", "Taco Bell",
        "Dunkin'", "Burger King", "Wendy's", "KFC", "Popeyes",
        "Dominos", "Donatos"
    )

    val placeTypes = listOf(
        // Travel & Transport
        "train_station", "airport", "bus_station", "subway_station", "hotel",

        // Shopping & Services
        "shopping_mall", "supermarket", "bank", "library", "post_office",

        // Leisure & Entertainment
        "cafe", "bakery", "restaurant", "bar", "movie_theater", "gym", "museum", "park", "casino", "zoo", "diner"
    )
}
