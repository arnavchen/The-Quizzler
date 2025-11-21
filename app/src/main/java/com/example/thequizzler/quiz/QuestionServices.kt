package com.example.thequizzler.quiz

import com.example.thequizzler.quiz.apiRepositories.PlacesRepository

/**
 * A service locator class that holds all the different repositories
 * and services needed for question generation.
 *
 * Instead of passing each repository individually, we pass this single
 * container object. When you add new APIs (e.g., Weather), you will
 * add the new repository here.
 */
data class QuestionServices(
    val placesRepository: PlacesRepository
    // Add future repositories here, e.g.:
    // val weatherRepository: WeatherRepository,
    // val stockMarketRepository: StockMarketRepository
)
