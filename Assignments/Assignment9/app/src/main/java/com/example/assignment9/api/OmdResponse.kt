package com.example.assignment9.api

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("Search")
    val search: List<MovieSearchItem>?,
    val totalResults: String?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)
data class MovieSearchItem(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Type") val type: String?,
    @SerializedName("Poster") val poster: String?
)
data class OmdbMovieDetailResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Rated") val rated: String?,
    @SerializedName("Released") val released: String?,
    @SerializedName("Runtime") val runtime: String?,
    @SerializedName("Genre") val genre: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Writer") val writer: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Poster") val poster: String?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)