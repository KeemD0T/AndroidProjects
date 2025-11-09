package com.example.assignment9



import com.example.assignment9.api.OmdApiService
import com.example.assignment9.api.RetrofitInstance.omdbApiService

import com.example.assignment9.api.MovieSearchResponse
import com.example.assignment9.api.OmdbMovieDetailResponse

class MovieRepository {
    private val apiKey = "d3b6da56" // IMPORTANT: Replace with your actual key

    /**
     * Searches for movies using the provided query string.
     * This function calls the apiService and returns the result.
     */
    suspend fun searchMovies(query: String): Result<MovieSearchResponse> {
        // This runCatching block correctly returns a Result object.
        return runCatching {
            omdbApiService.searchMovies(searchQuery = query, apiKey = apiKey)

        }
    }



    /**
     * Fetches detailed information for a specific movie using its IMDb ID.
     */
    suspend fun getMovieDetails(imdbId: String): Result<OmdbMovieDetailResponse> {
        // This runCatching block correctly returns a Result object.
        return runCatching {
            omdbApiService.getMovieDetails(imdbId = imdbId, apiKey = apiKey)
        }
    }
}

